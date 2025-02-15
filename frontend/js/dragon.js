import $ from "jquery";
import {addObject, putObject} from "./objects";
import {loadForm, redirectIfAuthenticated} from "./common/utils";
import './common/common';
import './common/modal';
import {form, setValues} from "./forms/dragonForm";
import {PaginationTable} from "./common/pagination";
import {common} from "./common/common";


const formPath = './forms/dragon_form.html'

class DragonPaginationTable extends PaginationTable {
    handleUpdate(id) {
        loadForm(formPath, 'update-modal-content',
            'update-dragon-form', function (formId) {
                const modal = $('#update-modal');
                modal.css('display', 'block');
                $('#update-dragon-form').data('id', id);

                $('#update-dragon-form').on('submit', function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/dragon", $(this).data('id'), formData);
                });

                form('update-dragon-form');
                setValues(id);
            });
    }
}

function getFormData(form) {
    return  {
        isChangeable: $(form).find('input[name="is-changeable-input"]').is(':checked'),
        name: $(form).find('#name-input').val(),
        coordinates: $(form).find('#coordinates-input').val(),
        killer: $(form).find('#killer-input').val(),
        cave: $(form).find('#cave-input').val(),
        head: $(form).find('#head-input').val(),
        age: $(form).find('#age-input').val(),
        speaking: $(form).find('input[name="speaking-input"]').is(':checked'),
        color: $(form).find('#color-input').val(),
        character: $(form).find('#dragon-character-input').val(),
    };
}


$(document).ready(function () {
    redirectIfAuthenticated();

    // Table and it's functions
    const table = new DragonPaginationTable("/dragon");
    common(table);

    loadForm(formPath, 'input-article', 'dragon-form', null);
    form('dragon-form');

    $(`#dragon-form`).on('submit', function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/dragon", formData);
    });

})
