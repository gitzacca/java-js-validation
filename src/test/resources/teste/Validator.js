define(['./ValidatorResult', './validators', './util'], function (ValidatorResult, validators, util) {

	function Validator() {
	}

	Validator.prototype.validate = function () {
		validators.notEmpty();
		validators.email();
		util.teste();

		return new ValidatorResult();
	};

	return Validator;

});