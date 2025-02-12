import $ from "jquery";
import {addObject, main} from "./objects";
import {redirectIfAuthenticated} from "./common/utils";

$(document).ready(function() {
    redirectIfAuthenticated();
    main("/dragonCave");

    $('#form').on('submit', function (event) {
        event.preventDefault();

        const formData = {
            isChangeable: $('#form input[name="is-changeable-input"]').is(':checked'),
            depth: $('#depth-input').val(),
            numberOfTreasures: $('#treasures-input').val()
        };

        addObject("/dragonCave", formData)
    });
})