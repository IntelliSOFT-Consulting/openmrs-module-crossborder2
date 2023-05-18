<%
    ui.includeJavascript("crossborder2", "kenyaemr/controllers/patientReg.js")

    def heading = config.heading ?: "Matching Patients"
%>
<div class="ke-panel-content">
    <fieldset id="clientVerificationSection">
        <legend>Client verification with Client Registry</legend>
        <table>
            <tr>
                <td class="ke-field-label">Country *</td>
                <td> </td>
                <td> </td>
                <td> </td>
                <td class="ke-field-label" style="white-space:nowrap;">Identifier Type *</td>
                <td> </td>
                <td> </td>
            </tr>
            <tr>
                <td>
                    <select name="nupi-verification-country" id="nupi-verification-country">
                        <option></option>
                    </select>
                </td>
                <td style="white-space:nowrap;"> <input type="checkbox" name="select-kenya-option-nupi-verification" value="Y" id="select-kenya-option-nupi-verification" /> Select Kenya </td>
                <td>
                    <div id="nupi-verification-country-msgBox" name="nupi-verification-country-msgBox" class="ke-warning">Country is Required</div>
                </td>
                <td></td>

                <td>
                    <select id="idType" name="idtype">
                        <option value="">Select a valid identifier type</option>

                    </select>
                </td>
                <td>
                    <input type="text" id="idValue" name="idValue" />
                </td>
                <td class="ke-field-instructions">
                    <div class="buttons-validate-identifiers">
                        <button type="button" class="ke-verify-button" id="validate-identifier">Validate Identifier</button>
                        <div class="wait-loading"></div>
                        <button type="button" class="ke-verify-button" id="show-cr-info-dialog">View Registry info</button>
                        <div class="message-validate-identifiers">
                            <label id="msgBox"></label>
                        </div>
                    </div>
                </td>
            </tr>
            <tr></tr>

        </table>
    </fieldset>
</div>

<style>
.ke-cr-client-exists {
    padding: 10px 20px;
    background-color: yellowgreen;
    color: #ffffff;
    font-weight: 200;
}

.ke-cr-client-not-found {
    padding: 10px 20px;
    background-color: darkred;
    color: #ffffff;
    font-weight: 200;
}

.wait-loading {
    margin-right: 5px;
    margin-left: 5px;
}

.buttons-validate-identifiers {
    float: left;
}

.buttons-validate-identifiers *{
    display: inline-block;
}

.message-validate-identifiers {
    margin-right: 5px;
    margin-left: 5px;
}

.buttons-post-create-patient {
    float: left;
}

.buttons-post-create-patient *{
    display: inline-block;
    margin: 0 auto;
}

.centre-content {
    display: flex;
    justify-content: center;
}

.text-wrap {
    white-space: pre-wrap;
}

.ke-cr-network-error {
    padding: 10px 20px;
    background-color: red;
    color: #ffffff;
    font-weight: 200;
}

.ke-verify-button {
    padding: 10px 20px;
    border-radius: 5px;
    background-color: #155cd2;
    color: #ffffff;
    /*font-family: Montserrat;*/
    font-size: 16px;
    font-weight: 200;
}

.ke-verify-button:hover {
    background-color:#002ead;
    transition: 0.7s;
}
</style>

