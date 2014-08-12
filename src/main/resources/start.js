'use strict';
/**
 * @name util
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
var util = {};

util.isArray = Array.isArray;

// Add some isType methods: isArguments, isFunction, isString, isNumber, isDate, isRegExp.
['Arguments', 'Function', 'String', 'Number', 'Date', 'RegExp', 'Object'].forEach(function (name) {
    util['is' + name] = function (obj) {
        return toString.call(obj) === '[object ' + name + ']';
    };
});

util.isEmpty = function (obj) {
    if (obj == null) {
        return true;
    }

    if (util.isArray(obj) || util.isString(obj) || util.isArguments(obj)) {
        return obj.length === 0;
    }

    if (util.isNumber(obj)) {
        return obj === 0;
    }

    for (var key in obj) {
        if (util.has(obj, key)) {
            return false;
        }
    }

    return true;
};

util.has = function (obj, key) {
    return obj != null && Object.prototype.hasOwnProperty.call(obj, key);
};

util.deep = function (obj, key, value) {
    var keys = key.replace(/\[(["']?)([^\1]+?)\1?\]/g, '.$2').replace(/^\./, '').split('.'),
        root,
        i = 0,
        n = keys.length;

    if (arguments.length > 2) {
        // Set deep value
        root = obj;
        n--;

        while (i < n) {
            key = keys[i++];
            obj = obj[key] = util.isObject(obj[key]) ? obj[key] : {};
        }

        obj[keys[i]] = value;

        value = root;
    } else {
        // Get deep value
        while ((obj = obj[keys[i++]]) != null && i < n) {
        }
        value = i < n ? void 0 : obj;
    }

    return value;
};

util.expressionToArray = function (expression) {
    return String(expression).replace(/[\(\),]/g, '|').replace(/ /g, '').replace(/\|$/, '').split('|');
};

util.clone = function clone(item) {
    if (!item) {
        return item;
    } // null, undefined values check

    var types = [ Number, String, Boolean ],
        result;

    // normalizing primitives if someone did new String('aaa'), or new Number('444');
    types.forEach(function (type) {
        if (item instanceof type) {
            result = type(item);
        }
    });

    if (typeof result == "undefined") {
        if (Object.prototype.toString.call(item) === "[object Array]") {
            result = [];
            item.forEach(function (child, index, array) {
                result[index] = clone(child);
            });
        } else if (typeof item == "object") {
            // testing that this is DOM
            if (item.nodeType && typeof item.cloneNode == "function") {
                var result = item.cloneNode(true);
            } else if (!item.prototype) { // check that this is a literal
                if (item instanceof Date) {
                    result = new Date(item);
                } else {
                    // it is an object literal
                    result = {};
                    for (var i in item) {
                        result[i] = clone(item[i]);
                    }
                }
            } else {
                // depending what you would like here,
                // just keep the reference, or create new object
                if (false && item.constructor) {
                    // would not advice to do that, reason? Read below
                    result = new item.constructor();
                } else {
                    result = item;
                }
            }
        } else {
            result = item;
        }
    }

    return result;
};


'use strict';
/**
 * br.com.dlbca.validation.Validator of email values
 *
 * @name validators/email
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
var email = function (value) {
    var isMail = /^[a-z0-9_]+@[a-z0-9_]+\.[a-z]{3}(\.[a-z]{2})?$/g;

    return isMail.test(value);
};


'use strict';
/**
 * br.com.dlbca.validation.Validator of not empty values
 *
 * @name validators/notEmpty
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
var notEmpty = function (value) {

    var type = Object.prototype.toString.call(value).replace('[object ', '').replace(']', '').toLowerCase();

    switch (type) {
        case 'string':
            return value !== '';
            break;
        case 'number':
            return value > 0;
            break;
        case 'array':
            return value.length > 0;
            break;
        case 'object':
            return Object.keys(value).length > 0;
            break;
        case 'null':
            return false;
            break;
        case 'undefined':
            return false;
            break;
        case 'domwindow':
            return false;
            break;
    }

    return false;
};


'use strict';
/**
 * Import all validators
 *
 * @name validators/index
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
var validators = {
    notEmpty: notEmpty,
    email: email
}


'use strict';
/**
 * @name br.com.dlbca.validation.Validator
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
var Validator = function (data, constrains) {
    var _data = data;
    var _constrains = constrains;

    var execValidations = function (data, constrains) {

        var errors = {};

        constrains.forEach(function (constrainsExpression) {
            var expressions = util.expressionToArray(constrainsExpression).reverse();

            var validatorName = expressions.pop();

            var values = [];
            expressions.forEach(function (value) {
                if (/^\$/.test(value)) {
                    values.unshift(util.deep(data, value.replace('$', '')));
                }
            });

            var validator = validators[validatorName];


            if (!validator.apply(this, values)) {
                var path = expressions.pop().replace('$', '');
                errors[path] = errors[path] || [];
                errors[path].push(validatorName);
            }
        });

        return errors;
    };

    if (data === undefined)
        throw new Error('Parametro data não pode ser vazio.');

    if (constrains === undefined)
        throw new Error('Parametro constrains não pode ser vazio.');

    this.getData = function () {
        return _data;
    };

    this.getConstrains = function () {
        return _constrains;
    };

    this.validate = function () {
        var errors = execValidations(this.getData(), this.getConstrains());
        return new ValidateResult(errors);
    };


};

Validator.version = '0.0.1';


'use strict';
/**
 * @name ValidateResult
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
var failuresApi = function (errors, field) {
    var self = this;

    this.all = function () {
        return errors[field];
    };

    Object.keys(validators).forEach(function (item) {
        var name = item.substring(0, 1).toUpperCase().concat(item.substring(1));

        var methodName = 'has{name}Passed'.replace('{name}', name);
        self[methodName] = function () {
            return (errors[field] || []).indexOf(item) !== -1;
        };
    });
};

var ValidateResult = function (errors) {

    var _errors = util.clone(errors);

    this.hasErrors = function () {

        return !util.isEmpty(this.getAllFailures());
    };

    this.getAllFailures = function () {
        return _errors;
    };

    this.forField = function (field) {
        return new failuresApi(this.getAllFailures(), field);
    };

    this.getError = function (path) {
        if (path === undefined) {
            return '';
        }
        return util.deep(this.getErrors(), path);
    };

};
