import $ from "jquery";
import './common/modal';
import {addObject, putObject} from "./objects";
import {loadForm, redirectIfAuthenticated} from "./common/utils";
import {common} from "./common/common";
import {form, setValues} from "./forms/dragonHeadForm";
import {PaginationTable} from "./common/pagination";

const formPath = './forms/dragon_head_form.html'

class DragonHeadPaginationTable extends PaginationTable {
    handleUpdate(id) {
        loadForm(formPath, 'update-modal-content',
            'update-dragon-head-form', function (formId) {
                const modal = $('#update-modal');
                modal.css('display', 'block');
                $('#update-dragon-head-form').data('id', id);

                $('#update-dragon-head-form').on('submit', function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/dragonHead", $(this).data('id'), formData);
                });

                form('update-dragon-head-form');
                setValues(id);
            });
    }
}

function getFormData(form) {
    return {
        isChangeable: $(form).find('input[name="is-changeable-input"]').is(':checked'),
        eyesCount: $(form).find('#eyes-count-input').val(),
        size: $(form).find('#size-input').val(),
    };
}

$(document).ready(function () {
    redirectIfAuthenticated();

    // Table and it's functions
    const table = new DragonHeadPaginationTable("/dragonHead");
    common(table);

    loadForm(formPath, 'input-article', 'dragon-head-form', null);
    form('dragon-head-form');

    $(`#dragon-head-form`).on('submit', function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/dragonHead", formData);
    });

})
