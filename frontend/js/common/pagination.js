import $ from 'jquery';
import { errorNotifies } from './error';
import * as c from './constants';
import {sound} from "./sound";
import * as u from "./utils";
import {redirectIfAuthenticated} from "./utils";

export class PaginationTable {
    constructor(url) {
        this.table = $("#pagination-table-body");
        this.paginationButtonsContainer = $("#pagination-button-container");
        this.buttons = $(".pagination-button");
        this.pageSizeButton = $("#page-size");

        this.addButtonListeners();
        let page = 0;
        this.url = url;
        this.changeSelectedButton(page);
        this.doRequest(page);

        window.sessionStorage.setItem("page", "0");
        window.sessionStorage.setItem("size", "10");

        this.pageSizeButton.on("change", (event) => this.changePageSize(event));

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
        this.doRequest(pageNumber, size);
        this.changeSelectedButton(pageNumber);
    }

    changePageSize(event) {
        event.preventDefault();
        let pageSize = parseInt(this.pageSizeButton.val());
        console.log(pageSize)
        window.sessionStorage.setItem("size", pageSize.toString());
        let pageNumber = parseInt(window.sessionStorage.getItem("page"));
        this.doRequest(pageNumber, pageSize);
    }

    doRequest(pageNumber, pageSize) {
        $.ajax({
            url: c.baseUrl + c.apiUrl + this.url,
            type: "GET",
            headers: u.getAuthHeader(),
            data: {"page": pageNumber, "size": pageSize},
            success: (response) => {
                this.updateBodyTable(response);
                this.updateButtons(response);
                window.sessionStorage.setItem("page", response.currentPage);
                sound.updateButtons();
                this.changeSelectedButton(response.currentPage);
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
        let pageNumber = response.currentPage;
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
        let pageNumber = parseInt(response.currentPage);
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

        this.table.append(row);
    }
}

