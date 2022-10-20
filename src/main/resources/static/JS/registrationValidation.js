"use strict";

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("form");
    form.addEventListener("submit", formSend);

    async function formSend(e) {
        e.preventDefault();
        let error = constValid(form);
        if(error === 0){
            form.submit();
        }
    }

    function constValid(form) {
        let errors = 0;
        let allRequiredElements = document.querySelectorAll(".required");
        for (var i = 0; i < allRequiredElements.length; i++) {
            let input = allRequiredElements[i];
            removeErrors(input);

            switch (input.id) {
                case "lastName":
                case "firstName":
                    if (!testFirstAndLastName(input)) {
                        addErrors(input);
                        errors++;
                    }
                    break;
                case "emailAddress":
                    if (!emailIsValid(input)) {
                        addErrors(input);
                        errors++;
                    }
                    break;
                case "username":
                    if (!usernameIsValid(input)) {
                        addErrors(input);
                        errors++;
                    }
                    break;
                case "password":
                case "secondPassword":
                    if (!passwordIsValid(input)) {
                        addErrors(input);
                        errors++;
                    }
                    break;
                case "phoneNumber":
                    if (!phoneNumberIsValid(input)) {
                        addErrors(input);
                        errors++;
                    }
            }
        }
        let firstPass = document.getElementById("password");
        let secondPass = document.getElementById("secondPassword");
        if (!isTheSamePasswords(firstPass.value, secondPass.value)) {
            firstPass.classList.add("error");
            secondPass.classList.add("error");
            errors++;
        }
        return errors;
    }

    function removeErrors(input) {
        let errorID = input.id + "Error";
        document.getElementById(errorID).style.display = "none";
        input.classList.remove("error");
    }

    function addErrors(input) {
        let errorID = input.id + "Error";
        document.getElementById(errorID).style.display = "block";
        input.classList.add("error");
    }

    function testFirstAndLastName(input) {

       // return /^[A-Za-zÀ-ßà-ÿ¨¸²³¯¿ªº]{1,40}$/.test(input.value);
        return /^[A-Za-z]{1,40}$/.test(input.value);
    }

    function usernameIsValid(input) {
        return /\w{4,64}/.test(input.value);
    }

    function emailIsValid(input) {
        return /^([A-Za-z0-9_-]+\.)*[A-Za-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$/.test(
            input.value
        );
    }

    function phoneNumberIsValid(input) {
        let phoneNumber = input.value;
        if (phoneNumber === "") {
            return true;
        }
        return /\(?\+?[0-9]{1,3}\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})? ?(\w{1,10}\s?\d{1,6})?/.test(
            phoneNumber
        );
    }

    function passwordIsValid(input) {
        return /\w{8,64}/.test(input.value);
    }

    function isTheSamePasswords(pass1, pass2) {
        return pass1 === pass2;
    }
});

