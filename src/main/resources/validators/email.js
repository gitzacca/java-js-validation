'use strict';
/**
 * Validator of email values
 *
 * @name validators/email
 * @author Guilherme Mangabeira Gregio <guilherme@gregio.net>
 */
define(function () {
	var email = function (value) {
		var isMail = /^[a-z0-9_]+@[a-z0-9_]+\.[a-z]{3}(\.[a-z]{2})?$/g;

		return isMail.test(value);
	};

	return email;
});