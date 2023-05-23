<%
    ui.decorateWith("kenyaui", "panel", [ heading: "Search for a Patient" ])

    ui.includeJavascript("crossborder2", "kenyaemr/controllers/patient.js")

    def defaultWhich = config.defaultWhich ?: "all"

    def id = config.id ?: ui.generateId();
%>
<form id="${ id }" ng-controller="AdvancedPatientSearchForm" ng-init="init('${ defaultWhich }')">
    <label  class="ke-field-label">Which patients</label>
    <span class="ke-field-content" style="font-size: 12px;">
        <input type="radio" ng-model="which" ng-change="updateSearch()" value="all" /> All
    &nbsp;&nbsp;
        <input type="radio" ng-model="which" ng-change="updateSearch()" value="checked-in" /> Only Checked In

        <input type="radio" ng-model="which" ng-change="updateSearch()" value="cross-border" /> Cross border
    </span>
    <label class="ke-field-label">Cross-border ID</label>
    <span class="ke-field-content">
        <input type="text" name="cb-id-query" ng-model="cbIdQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" />
    </span>
   <label class="ke-field-label">Patient clinic number</label>
    <span class="ke-field-content">
        <input type="text" name="clinic-no-query" ng-model="clinicNoQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" />
    </span>
    <label class="ke-field-label">Names</label>
    <span class="ke-field-content">
        <input type="text" name="name-query" ng-model="nameQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" placeholder="Name" />
    </span>
    <label class="ke-field-label">Sex</label>
    <span class="ke-field-content">
        <input type="radio" name="gender-query" ng-model="genderQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" value="female" />Female
        <input type="radio" name="gender-query" ng-model="genderQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" value="male" />Male
    </span>
    <span class="ke-field-content">
        <label class="ke-field-label">Search MPI <input type="checkbox" name="select-mpi" ng-model="selectMpiQuery" ng-change="onQueryChange()" style="margin-left: 35px" /></label>
    </span>
    <span class="ke-field-content" ng-if="selectMpiQuery">
        <select name="mpi-query" ng-model="mpiQuery" ng-change="onQueryChange()" style="width: 260px" placeholder="Select MPI" >
            <option></option>
            <option value="crossborder-mpi">Cross border MPI</option>
        </select>
    </span>
    <fieldset>
        <div class="ke-panel-footer centre-content">
                <button type="button" id="search-patient" style="margin-right: 5px; margin-left: 5px;" ng-click="updateSearch()">
                    <img src="${ui.resourceLink("kenyaui", "images/buttons/patient_search.png")}"/> Search
                </button>
        </div>
    </fieldset>
</form>
