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

            if(!passwordIsValid(input)){
                addErrors(input);
                errors++;
            }
        }
        let firstPass = document.getElementById("newPassword");
        let secondPass = document.getElementById("confirmedNewPassword");
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

    function passwordIsValid(input) {
        return /\w{8,64}/.test(input.value);
    }

    function isTheSamePasswords(pass1, pass2) {
        return pass1 === pass2;
    }
});