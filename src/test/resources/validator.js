define('./validator',['./validationResult'], function(ValidationResult){
	var Validator = function (data, constrains) {
	    this.validate = function () {
	        return new ValidationResult();
	    };
	};
	
	return Validator;
});

