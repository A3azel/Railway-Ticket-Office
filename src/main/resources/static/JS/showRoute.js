function initMap() {
    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer();
    var kyiv = new google.maps.LatLng(50.450001, 30.523333);
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 7,
        center: kyiv,
    });
    directionsRenderer.setMap(map);

    const citesMap = new Map();
    citesMap.set('Київ','Kyiv');
    citesMap.set('Львів','Lviv');
    citesMap.set('Харків','Kharkiv');
    citesMap.set('Одеса','Odesa');
    citesMap.set('Житомир','Zhytomyr');
    citesMap.set('Херсон','Kherson');

    directionsService
        .route({
            origin: {
                query: citesMap.get(document.getElementById("start").textContent),
            },
            destination: {
                query: citesMap.get(document.getElementById("end").textContent),
            },
            travelMode: "TRANSIT",
            //modes: ["TRAIN"],
        })
        .then((response) => {
            directionsRenderer.setDirections(response);
        })
        .catch((e) => window.alert("Directions request failed due to " + status));
}

window.initMap = initMap;

