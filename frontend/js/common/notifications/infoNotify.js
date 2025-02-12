import Notify from "simple-notify";
import * as constants from "../constants"

export default class InfoNotify extends Notify {
    constructor(title, text) {
        super({
            title: title,
            text: text,
            autotimeout: constants.timeout,
            autoclose: constants.autoClose,
            status: constants.infoStatus
        });
    }
}