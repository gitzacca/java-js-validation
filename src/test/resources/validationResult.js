define(function(){
	var ValidationResult = function () {
	    this.hasErrors = function () {
	        return false;
	    }

	    this.getAllFailures = function () {
	        return {
	            name: ['notEmpty']
	        };
	    }

	    this.getError = function (name) {
	        return ['notEmpty'];
	    }
	}
	
	return ValidationResult;
});