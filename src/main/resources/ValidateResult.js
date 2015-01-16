'use strict';
/**
 * @name ValidateResult
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
define(['./util', './validators'], function (util, validators) {

	var failuresApi = function (errors, field) {
		var self = this;

		this.all = function () {
			return errors[field];
		};

		Object.keys(validators).forEach(function (item) {
			var name = item.replace(/(^.)/, function (char) {
				return char.toUpperCase();
			});

			var methodName = 'has{name}Passed'.replace('{name}', name);
			self[methodName] = function () {
				return (errors[field]||[]).indexOf(item) !== -1;
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

		this.for = function (field) {
			return new failuresApi(this.getAllFailures(), field);
		};

		this.getError = function (path) {
			if (path === undefined) {
				return '';
			}
			return util.deep(this.getAllFailures(), path);
		};

	};

	return ValidateResult;
});