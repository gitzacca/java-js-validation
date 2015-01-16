define('./validatorWithoutValidateResult',[], function(){
	
	var Validator = function (data, constrains) {
		this.validate = function () {
			return new ValidateResult();
		};
	}
	
	return Validator;
	
});


var ValidateResult = function () {

};