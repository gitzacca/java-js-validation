'use strict';
/**
 * Validator of not empty values
 *
 * @name validators/notEmpty
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
define(function () {
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

	return notEmpty;
});