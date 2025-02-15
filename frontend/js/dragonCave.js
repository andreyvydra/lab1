import $ from "jquery";
import './common/modal';
import {addObject, putObject} from "./objects";
import {loadForm, redirectIfAuthenticated} from "./common/utils";
import {common} from "./common/common";
import {form, setValues} from "./forms/dragonCaveForm";
import {PaginationTable} from "./common/pagination";

const formPath = './forms/dragon_cave_form.html'

class DragonHeadPaginationTable extends PaginationTable {
    handleUpdate(id) {
        loadForm(formPath, 'update-modal-content',
            'update-dragon-cave-form', function (formId) {
                const modal = $('#update-modal');
                modal.css('display', 'block');
                $('#update-dragon-cave-form').data('id', id);

                $('#update-dragon-cave-form').on('submit', function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/dragonCave", $(this).data('id'), formData);
                });

                form('update-dragon-cave-form');
                setValues(id);
            });
    }
}

function getFormData(form) {
    return {
        isChangeable: $(form).find('input[name="is-changeable-input"]').is(':checked'),
        depth: $(form).find('#depth-input').val(),
        numberOfTreasures: $(form).find('#treasures-input').val()
    };
}

$(document).ready(function () {
    redirectIfAuthenticated();

    // Table and it's functions
    const table = new DragonHeadPaginationTable("/dragonCave");
    common(table);

    loadForm(formPath, 'input-article', 'dragon-cave-form', null);
    form('dragon-cave-form');

    $(`#dragon-cave-form`).on('submit', function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/dragonCave", formData);
    });

})