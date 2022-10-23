(function addPopularDirections(){
    "use strict";
    const clickButton = document.querySelector(".popularDirections");
    clickButton.addEventListener("click",function () {
        const key = event.target.value;
        let coupleOfCities = key.split("-");
        let startCity = document.querySelector("#from");
        startCity.value = coupleOfCities[0];
        let arrivalCity = document.querySelector("#where");
        arrivalCity.value = coupleOfCities[1];
    });
}());

(function setDate(){
    "use strict";
    let date = new Date();
    date.setMinutes(date.getMinutes() - date.getTimezoneOffset());
    date.setHours(date.getHours() + 1);
    let currentDate = date.toISOString().substring(0, 10);
    let currentTime = date.toISOString().substring(11, 16);
    const dataElement = document.getElementById('startDate');
    const timeElement = document.getElementById('startTime');
    dataElement.value = currentDate;
    timeElement.value = currentTime;
}());

