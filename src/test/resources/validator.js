var Validator = function (data, constrains) {
    this.validate = function () {
        return new ValidationResult();
    };
};

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