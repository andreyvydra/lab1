import $ from 'jquery';
import { errorNotifies } from './error';
import * as c from './constants';
import {sound} from "./sound";
import * as u from "./utils";

class PaginationTable {
    constructor() {
        this.table = $("#pagination-table-body");
        this.buttonsContainer = $("#pagination-button-container");
        this.buttons = $(".pagination-button");

        this.addButtonListeners();
        let page = 0;
        this.changeSelectedButton(page);
        this.doRequest(page);
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
        this.doRequest(pageNumber);
        this.changeSelectedButton(pageNumber);
    }

    doRequest(pageNumber) {
        $.ajax({
            url: c.baseUrl + c.apiUrl + '/location',
            type: "GET",
            headers: u.getAuthHeader(),
            data: {"page": pageNumber},
            success: (response) => {
                this.updateBodyTable(response);
                this.updateButtons(response);
                window.sessionStorage.setItem("page", response.currentPage);
                sound.updateButtons();
                this.changeSelectedButton(response.currentPage);
            },
            error: (xhr) => {
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
        this.buttonsContainer.empty();
    }

    addButton(pageNumber) {
        this.buttonsContainer.append(`<button class='pagination-button'>${pageNumber}</button>`);
    }

    addSpan() {
        this.buttonsContainer.append("<span>...</span>");
    }

    addBodyTable(values) {
        this.table.append(`
            <tr>
                <td>${values.id}</td>
                <td>${values.name}</td>
                <td>${values.x}</td>
                <td>${values.y}</td>
            </tr>
        `);
    }
}

export const paginationTable = new PaginationTable();
