'use strict';
/**
 * @name Validator
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
define(['./ValidateResult', './validators', './util'], function (ValidateResult, validators, util) {
	var Validator = function (data, constrains) {
		var _data = data;
		var _constrains = constrains;

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

		var execValidations = function (data, constrains) {
			var errors = {};

			util.forEach(constrains, function (constrainsExpression) {
				var expressions = util.expressionToArray(constrainsExpression).reverse();

				var validatorName = expressions.pop();

				var values = [];
				util.forEach(expressions, function (value) {
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
	};

	Validator.version = '0.0.1';

	return Validator;
});