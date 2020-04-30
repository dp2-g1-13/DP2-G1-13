var autocomplete;

function initAutocomplete() {
    var input = $("#city.form-control")[0];
    autocomplete = new google.maps.places.Autocomplete(
        input, {types: ['geocode']});
    google.maps.event.addListener(autocomplete, 'place_changed', function() {
        place = autocomplete.getPlace();
    });
}

var map;
function initMap(latitude, longitude, zoom) {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: latitude, lng: longitude},
        zoom: zoom
    });
}

function createMarkers(advertisements) {
    for (var i = 0; i < advertisements.length; i++) {
        var infowindow = new google.maps.InfoWindow;
        var marker = new google.maps.Marker({
            position: advertisements[i].position,
            map: map
        });
        google.maps.event.addListener(marker, 'click', (function (marker, i) {
            return function () {
                infowindow.setContent(advertisements[i].title);
                infowindow.open(map, marker);
            }
        })(marker, i));
    }
}

function createMarker(latitude, longitude) {
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(latitude, longitude),
        map: map
    });
}

