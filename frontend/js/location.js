import $ from "jquery";
import {addObject, pagination} from "./objects";
import {redirectIfAuthenticated} from "./common/utils";

$(document).ready(function() {
    redirectIfAuthenticated();
    pagination("/location");

    $('#form').on('submit', function (event) {
        event.preventDefault();

        const formData = {
            isChangeable: $('#form input[name="is-changeable-input"]').is(':checked'),
            x: $('#x-input').val(),
            y: $('#y-input').val(),
            name: $('#name-input').val()
        };

        addObject("/location", formData)
    });
})