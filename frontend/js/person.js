import $ from "jquery";
import {addObject, putObject} from "./objects";
import {getAuthHeader, loadForm, redirectIfAuthenticated, getCookie, decodeJWT} from "./common/utils";
import "./common/common";
import "./common/modal";
import {form, setValues} from "./forms/personForm";
import {PaginationTable} from "./common/pagination";
import {common} from "./common/common";
import WarningNotify from "./common/notifications/warningNotify";
import {errorNotifies} from "./common/error";
import InfoNotify from "./common/notifications/infoNotify";
import * as c from "./common/constants";

const formPath = "./forms/person_form.html";

class PersonPaginationTable extends PaginationTable {
    addBodyTable(values) {
        const row = $("<tr>");
        const orderedKeys = [
            "id",
            "isChangeable",
            "user",
            "name",
            "eyeColor",
            "hairColor",
            "location",
            "passportID",
            "nationality",
            "height"
        ];
        orderedKeys.forEach(k => row.append($("<td>").text(values[k])));
        const {payload} = decodeJWT(getCookie("access_token")) || {};
        const actionsCell = $("<td>");
        const isOwner = payload && values.user === payload.id;
        const isAdmin = payload && payload.role === "ROLE_ADMIN";
        if (isOwner || (isAdmin && values.isChangeable)) {
            const updateButton = $("<button>")
                .html("<div>")
                .addClass("action-button update-button")
                .on("click", () => this.handleUpdate(values.id));
            const deleteButton = $("<button>")
                .html("<div>")
                .addClass("action-button delete-button")
                .on("click", () => this.handleDelete(values.id));
            actionsCell.append(updateButton, deleteButton);
        }
        row.append(actionsCell);
        this.table.append(row);
    }

    handleUpdate(id) {
        loadForm(
            formPath,
            "update-modal-content",
            "update-person-form",
            function () {
                const modal = $("#update-modal");
                modal.css("display", "block");
                $("#update-person-form").data("id", id);

                $("#update-person-form").on("submit", function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/person", $(this).data("id"), formData);
                });

                form("update-person-form");
                setValues(id);
            }
        );
    }
}

function getFormData(formElement) {
    return {
        isChangeable: $(formElement).find('input[name="is-changeable-input"]').is(":checked"),
        name: $(formElement).find("#name-input").val(),
        eyeColor: $(formElement).find("#color-eyes-input").val(),
        hairColor: $(formElement).find("#color-hair-input").val(),
        nationality: $(formElement).find("#nationality-input").val(),
        location: $(formElement).find("#location-input").val(),
        height: $(formElement).find("#height-input").val(),
        passportID: $(formElement).find("#passport-input").val()
    };
}

$(document).ready(function () {
    redirectIfAuthenticated();

    const table = new PersonPaginationTable("/person");
    common(table);

    loadForm(formPath, "input-article", "person-form", null);
    form("person-form");

    $("#person-form").on("submit", function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/person", formData);
    });

    $("#upload-csv-form").on("submit", function (event) {
        event.preventDefault();
        const fileInput = $("#csv-file")[0];
        if (fileInput.files.length > 0) {
            const formData = new FormData();
            formData.append("file", $("#csv-file")[0].files[0]);

            $.ajax({
                url: `${c.baseUrl}${c.apiUrl}/person/import`,
                headers: getAuthHeader(),
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    new InfoNotify("Импорт", response.message);
                },
                error: function (error) {
                    errorNotifies(error);
                }
            });
        } else {
            new WarningNotify(
                "Внимание",
                "Пожалуйста, выберите CSV-файл для загрузки!"
            );
        }
    });
});
