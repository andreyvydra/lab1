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
        coordinates: $(formEl).find('#coordinates-list div.selected').data('id') || $(formEl).find('#coordinates-input').val(),
        owner: $(formEl).find('#owner-list div.selected').data('id') || $(formEl).find('#owner-input').val(),
        manufacturer: $(formEl).find('#manufacturer-list div.selected').data('id') || $(formEl).find('#manufacturer-input').val() || null,
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

    $("#delete-by-owner").on("click", function () {
        const ownerId = parseInt($("#delete-by-owner-input").val());
        $.ajax({
            url: c.baseUrl + c.apiUrl + '/product/deleteOneByOwner',
            type: 'DELETE',
            headers: getAuthHeader(),
            data: { owner: ownerId },
            success: function (response) {
                new InfoNotify("Удаление", `Удалён продукт ID=${response.id}`);
                table.doRequest(window.sessionStorage.getItem("page"), window.sessionStorage.getItem("size"));
            },
            error: function (xhr) { errorNotifies(xhr); }
        });
    });

    $("#starts-with").on("click", function () {
        const prefix = $("#starts-with-input").val();
        $.ajax({
            url: c.baseUrl + c.apiUrl + '/product/nameStartsWith',
            type: 'GET',
            headers: getAuthHeader(),
            data: { prefix },
            success: function (response) {
                new InfoNotify("Результат", `Найдено: ${response.length}`);
            },
            error: function (xhr) { errorNotifies(xhr); }
        });
    });

    $("#rating-less").on("click", function () {
        const max = parseInt($("#rating-less-input").val());
        $.ajax({
            url: c.baseUrl + c.apiUrl + '/product/ratingLess',
            type: 'GET',
            headers: getAuthHeader(),
            data: { max },
            success: function (response) {
                new InfoNotify("Результат", `Найдено: ${response.length}`);
            },
            error: function (xhr) { errorNotifies(xhr); }
        });
    });

    $("#by-manufacturer").on("click", function () {
        const manufacturer = parseInt($("#by-manufacturer-input").val());
        $.ajax({
            url: c.baseUrl + c.apiUrl + '/product/byManufacturer',
            type: 'GET',
            headers: getAuthHeader(),
            data: { manufacturer },
            success: function (response) {
                new InfoNotify("Результат", `Найдено: ${response.length}`);
            },
            error: function (xhr) { errorNotifies(xhr); }
        });
    });

    $("#by-unit").on("click", function () {
        const unitOfMeasure = $("#by-unit-input").val();
        $.ajax({
            url: c.baseUrl + c.apiUrl + '/product/byUnit',
            type: 'GET',
            headers: getAuthHeader(),
            data: { unitOfMeasure },
            success: function (response) {
                new InfoNotify("Результат", `Найдено: ${response.length}`);
            },
            error: function (xhr) { errorNotifies(xhr); }
        });
    });
});
