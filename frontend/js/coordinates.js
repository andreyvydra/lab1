import $ from "jquery";
import './common/modal';
import {addObject, putObject} from "./objects";
import {loadForm, redirectIfAuthenticated, getCookie, decodeJWT} from "./common/utils";
import {common} from "./common/common";
import {form, setValues} from "./forms/coordinatesForm";
import {PaginationTable} from "./common/pagination";

const formPath = './forms/coordinates_form.html'

class CoordinatesPaginationTable extends PaginationTable {
    addBodyTable(values) {
        const row = $('<tr>');
        const orderedKeys = ['id','isChangeable','user','x','y'];
        orderedKeys.forEach(k => row.append($('<td>').text(values[k])));
        const { payload } = decodeJWT(getCookie("access_token")) || {};
        const actionsCell = $('<td>');
        const isOwner = payload && values.user === payload.id;
        const isAdmin = payload && payload.role === "ROLE_ADMIN";
        if (isOwner || (isAdmin && values.isChangeable)) {
            const updateButton = $('<button>').html('<div>').addClass('action-button update-button').on('click', () => this.handleUpdate(values.id));
            const deleteButton = $('<button>').html('<div>').addClass('action-button delete-button').on('click', () => this.handleDelete(values.id));
            actionsCell.append(updateButton, deleteButton);
        }
        row.append(actionsCell);
        this.table.append(row);
    }
    handleUpdate(id) {
        loadForm(formPath, 'update-modal-content',
            'update-coordinates-form', function (formId) {
                const modal = $('#update-modal');
                modal.css('display', 'block');


                $('#update-coordinates-form').data('id', id);

                $('#update-coordinates-form').on('submit', function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/coordinates", $(this).data('id'), formData);
                });

                form('update-coordinates-form');
                setValues(id);
            });
    }
}

function getFormData(form) {
    return {
        isChangeable: $(form).find('input[name="is-changeable-input"]').is(':checked'),
        x: $(form).find('#x-input').val(),
        y: $(form).find('#y-input').val(),
    };
}

$(document).ready(function () {
    redirectIfAuthenticated();

    // Table and it's functions
    const table = new CoordinatesPaginationTable("/coordinates");
    common(table);

    loadForm(formPath, 'input-article', 'coordinates-form', null);
    form('coordinates-form');

    $(`#coordinates-form`).on('submit', function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/coordinates", formData);
    });

})
