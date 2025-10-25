import $ from "jquery";
import {addObject, putObject} from "./objects";
import {loadForm, redirectIfAuthenticated, getCookie, decodeJWT} from "./common/utils";
import './common/common';
import './common/modal';
import {form, setValues} from "./forms/productForm";
import {PaginationTable} from "./common/pagination";
import {common} from "./common/common";

const formPath = './forms/product_form.html';

class ProductPaginationTable extends PaginationTable {
    addBodyTable(values) {
        const row = $('<tr>');
        const orderedKeys = [
            'id','isChangeable','user','name','coordinates','creationDate',
            'manufacturer','unitOfMeasure','price','manufactureCost','rating','owner'
        ];
        orderedKeys.forEach(k => {
            const cell = $('<td>').text(values[k]);
            row.append(cell);
        });

        const { payload } = decodeJWT(getCookie("access_token")) || {};
        const actionsCell = $('<td>');
        const isOwner = payload && values.user === payload.id;
        const isAdmin = payload && payload.role === "ROLE_ADMIN";
        if (isOwner || (isAdmin && values.isChangeable)) {
            const updateButton = $('<button>')
                .html('<div>')
                .addClass('action-button update-button')
                .on('click', () => this.handleUpdate(values.id));
            const deleteButton = $('<button>')
                .html('<div>')
                .addClass('action-button delete-button')
                .on('click', () => this.handleDelete(values.id));
            actionsCell.append(updateButton, deleteButton);
        }
        row.append(actionsCell);
        this.table.append(row);
    }
    handleUpdate(id) {
        loadForm(formPath, 'update-modal-content',
            'update-product-form', function () {
                const modal = $('#update-modal');
                modal.css('display', 'block');
                $('#update-product-form').data('id', id);

                $('#update-product-form').on('submit', function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/product", $(this).data('id'), formData);
                });

                form('update-product-form');
                setValues(id);
            });
    }
}

function getFormData(formEl) {
    return {
        isChangeable: $(formEl).find('input[name="is-changeable-input"]').is(':checked'),
        name: $(formEl).find('#name-input').val(),
        coordinates: $(formEl).find('#coordinates-input').val(),
        owner: $(formEl).find('#owner-input').val(),
        manufacturer: $(formEl).find('#manufacturer-input').val() || null,
        unitOfMeasure: $(formEl).find('#unit-input').val() || null,
        price: $(formEl).find('#price-input').val() || null,
        manufactureCost: $(formEl).find('#manufacture-cost-input').val() || null,
        rating: $(formEl).find('#rating-input').val()
    };
}

$(document).ready(function () {
    redirectIfAuthenticated();

    const table = new ProductPaginationTable("/product");
    common(table);

    loadForm(formPath, 'input-article', 'product-form', null);
    form('product-form');

    $('#product-form').on('submit', function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/product", formData);
    });
});
