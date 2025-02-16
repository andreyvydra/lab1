import $ from 'jquery';
import {errorNotifies} from './error';
import * as c from './constants';
import {sound} from "./sound";
import * as u from "./utils";
import {decodeJWT, getAuthHeader, getCookie, loadForm, redirectIfAuthenticated} from "./utils";
import InfoNotify from "./notifications/infoNotify";

export class PaginationTable {
    constructor(url) {
        this.table = $("#pagination-table-body");
        this.paginationButtonsContainer = $("#pagination-button-container");
        this.buttons = $(".pagination-button");
        this.pageSizeButton = $("#page-size");

        this.addButtonListeners();
        this.addSortHandlers();
        this.addSearchListener();

        let page = 0;
        this.url = url;
        this.changeSelectedButton(page);
        this.doRequest(page, 10);

        window.sessionStorage.setItem("page", "0");
        window.sessionStorage.setItem("size", "10");
        window.sessionStorage.removeItem("sortField");
        window.sessionStorage.removeItem("ascending");
        window.sessionStorage.removeItem("filter");


        this.pageSizeButton.on("change", (event) => this.changePageSize(event));

    }

    addSearchListener() {
        $("#search-input").on("input", (event) => {
            const query = $(event.target).val();
            window.sessionStorage.setItem("filter", query);
            this.doRequest(
                window.sessionStorage.getItem("page"),
                window.sessionStorage.getItem("size"),
                window.sessionStorage.getItem("sortField"),
                window.sessionStorage.getItem("ascending"),
                query
            );
        })
    }

    addButtonListeners() {
        this.buttons.on("click", (event) => this.getPage(event));
    }

    changeSelectedButton(value) {
        this.buttons.removeClass("button-is-selected");
        this.buttons.each((_, button) => {
            if (parseInt($(button).text()) - 1 === value) {
                $(button).addClass("button-is-selected");
            }
        });
    }

    getPage(event) {
        event.preventDefault();
        let pageNumber = parseInt($(event.currentTarget).text()) - 1;
        window.sessionStorage.setItem("page", pageNumber.toString());
        let size = parseInt(window.sessionStorage.getItem("size"));
        this.doRequest(pageNumber, size,
            window.sessionStorage.getItem("sortField"),
            window.sessionStorage.getItem("ascending"),
            window.sessionStorage.getItem("filter"));
        this.changeSelectedButton(pageNumber);
    }

    changePageSize(event) {
        event.preventDefault();
        let pageSize = parseInt(this.pageSizeButton.val());
        console.log(pageSize)
        window.sessionStorage.setItem("size", pageSize.toString());
        let pageNumber = parseInt(window.sessionStorage.getItem("page"));
        this.doRequest(
            pageNumber, pageSize,
            window.sessionStorage.getItem("sortField"),
            window.sessionStorage.getItem("ascending"),
            window.sessionStorage.getItem("filter")
        );
    }

    addSortHandlers() {
        const headers = $('th');
        headers.on('click', (event) => {
            const sortField = $(event.target).data('name');
            if (sortField == null) return;

            const currentIndicator = $(event.target).find('.sort-indicator');
            let ascending = true;
            if (currentIndicator.length > 0) {
                ascending = currentIndicator.text() === ' ▼';
            }

            window.sessionStorage.setItem("sortField", sortField);
            window.sessionStorage.setItem("ascending", ascending.toString());

            console.log(sortField)

            this.doRequest(
                window.sessionStorage.getItem("page"),
                window.sessionStorage.getItem("size"),
                sortField,
                ascending,
                window.sessionStorage.getItem("filter")
            );

            headers.find('.sort-indicator').remove();
            const indicator = $('<span>').addClass('sort-indicator').text(ascending ? ' ▲' : ' ▼');
            $(event.target).append(indicator);
        });
    }


    doRequest(pageNumber, pageSize, sortField = null, ascending = null, filter = '') {
        console.log(sortField, ascending)

        const requestData = {
            page: pageNumber,
            size: pageSize,
            filter: filter
        };

        if (sortField) {
            requestData.sortField = sortField;
            requestData.ascending = ascending;
        }

        $.ajax({
            url: c.baseUrl + c.apiUrl + this.url,
            type: "GET",
            headers: u.getAuthHeader(),
            data: requestData,
            success: (response) => {
                if (response.currentPage >= response.totalPages && response.totalPages !== 0) {
                    window.sessionStorage.setItem("page", Math.max(response.totalPages - 1, 0).toString());
                    requestData.page = Math.max(response.totalPages - 1, 0);
                    this.doRequest(
                        requestData.page,
                        requestData.size,
                        requestData.sortField,
                        requestData.ascending,
                        requestData.filter
                    )
                } else {
                    this.updateBodyTable(response);
                    this.updateButtons(response);
                    window.sessionStorage.setItem("page", response.currentPage);
                    sound.updateButtons();
                    this.changeSelectedButton(response.currentPage);
                }
            },
            error: (xhr) => {
                if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
                errorNotifies(xhr);
            },
        });
    }

    updateBodyTable(response) {
        this.table.empty(); // Очищаем таблицу
        response.content.forEach((item) => {
            this.addBodyTable(item);
        });
    }

    updateButtons(response) {
        this.deleteButtons();
        let pageNumber = response.currentPage + 1;
        let totalPages = parseInt(response.totalPages)
        if (totalPages <= c.minPageCount) {
            this.addMinimumButtonsWidget(response);
        } else if (pageNumber < c.minPageCount) {
            this.addLeftButtonsWidget(response);
        } else if (totalPages - pageNumber < c.minPageCount - 1) {
            this.addRightButtonsWidget(response);
        } else {
            this.addMiddleButtonsWidget(response);
        }

        this.buttons = $(".pagination-button"); // Обновляем коллекцию кнопок
        this.addButtonListeners();
    }

    addMiddleButtonsWidget(response) {
        let pageNumber = parseInt(response.currentPage) + 1;
        this.addButton(1);
        this.addSpan();
        for (let i = pageNumber - 2; i <= pageNumber + 2; i++) {
            this.addButton(i);
        }
        this.addSpan();
        this.addButton(response.totalPages);
    }

    addRightButtonsWidget(response) {
        this.addButton(1);
        this.addSpan();
        for (let i = response.totalPages - 4; i <= response.totalPages; i++) {
            this.addButton(i);
        }
    }

    addLeftButtonsWidget(response) {
        for (let i = 1; i <= 5; i++) {
            this.addButton(i);
        }
        this.addSpan();
        this.addButton(response.totalPages);
    }

    addMinimumButtonsWidget(response) {
        for (let i = 1; i <= response.totalPages; i++) {
            this.addButton(i);
        }
    }

    deleteButtons() {
        this.paginationButtonsContainer.empty();
    }

    addButton(pageNumber) {
        this.paginationButtonsContainer.append(`<button class='pagination-button'>${pageNumber}</button>`);
    }

    addSpan() {
        this.paginationButtonsContainer.append("<span>...</span>");
    }

    addBodyTable(values) {
        const row = $('<tr>');

        for (const key in values) {
            if (values.hasOwnProperty(key)) {
                const cell = $('<td>').text(values[key]);
                row.append(cell);
            }
        }

        const {_, payload, __} = decodeJWT(getCookie("access_token"));
        const actionsCell = $('<td>');
        const isOwner = values.user === payload.id;
        const isAdmin = payload.role === "ROLE_ADMIN";

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

    }

    handleDelete(id) {
        if (confirm("Вы уверены, что хотите удалить эту запись?")) {
            $.ajax({
                url: c.baseUrl + c.apiUrl + this.url + '/' + id + '/delete',
                type: "DELETE",
                headers: u.getAuthHeader(),
                success: (response) => {
                    new InfoNotify("Запись успешно удалена");
                    this.doRequest(
                        window.sessionStorage.getItem("page"),
                        window.sessionStorage.getItem("size"),
                        window.sessionStorage.getItem("sortField"),
                        window.sessionStorage.getItem("ascending"),
                        window.sessionStorage.getItem("filter")
                    );
                },
                error: (xhr) => {
                    if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
                    errorNotifies(xhr);
                },
            });
        }
    }
}

