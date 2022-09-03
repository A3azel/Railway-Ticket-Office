"use strict";

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("form");
    form.addEventListener("submit", formSend);

    async function formSend(e) {
        e.preventDefault();
        let error = constValid(form);
    }

    function constValid(form) {
        let allRequiredElements = document.querySelectorAll(".required");
        event.preventDefault();
        for (var i = 0; i < allRequiredElements.length; i++) {
            let input = allRequiredElements[i];
            removeErrors(input);
            switch (input.id) {
                case 'firstName' || 'lastName':
                    if (!testFirstAndLastName(input)) {
                        addErrors(input);
                    }
                    break;
                case 'emailAddress':
                    if (!emailIsValid(input)) {
                        addErrors(input);
                    }
                    break;
            }
        }
    }


    function removeErrors(input){
        input.parentElement.classList.remove('errors');
        input.classList.remove('errors');
    }

    function addErrors(input){
        input.parentElement.classList.add('errors');
        input.classList.add('errors');
    }

    function testFirstAndLastName(input) {
        return /^[A-Z\p{Script=Cyrillic}][A-Za-z\p{Script=Cyrillic}]{1,40}$/.test(input.value);
    }

    function emailIsValid(input) {
        return /^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$/.test(input.value);
    }
});
