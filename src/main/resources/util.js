'use strict';
/**
 * @name util
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
define(function () {
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
		expression = new String(expression);
		return expression.replace(/[\(\),]/g, '|').replace(/ /g, '').replace(/\|$/, '').split('|');
	};

	util.clone = function clone(item) {
		if (!item) {
			return item;
		} // null, undefined values check

		var types = [Number, String, Boolean],
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

	util.forEach = function (array, iteratee) {
		var index = -1,
			length = array.length;

		while (++index < length) {
			if (iteratee(array[index], index, array) === false) {
				break;
			}
		}
		return array;
	};

	return util;
});