<%
    ui.decorateWith("kenyaui", "panel", [ heading: "Search for a Patient" ])

    ui.includeJavascript("crossborder2", "controllers/patient.js")

    def defaultWhich = config.defaultWhich ?: "all"

    def id = config.id ?: ui.generateId();
%>
<form id="${ id }" ng-controller="AdvancedPatientSearchForm" ng-init="init('${ defaultWhich }')">
    <label  class="ke-field-label">Which patients</label>
    <span class="ke-field-content">
        <input type="radio" ng-model="which" ng-change="updateSearch()" value="all" /> All
    &nbsp;&nbsp;
        <input type="radio" ng-model="which" ng-change="updateSearch()" value="checked-in" /> Only Checked In

        <input type="radio" ng-model="which" ng-change="updateSearch()" value="cross-border" /> Cross border
    </span>
    <label class="ke-field-label">Cross-border ID</label>
    <span class="ke-field-content">
        <input type="text" name="cb-id-query" ng-model="cb-id-query" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" />
    </span>
    <label class="ke-field-label">Patient clinic number</label>
    <span class="ke-field-content">
        <input type="text" name="clinic-no-query" ng-model="clinic-no-query" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" />
    </span>
    <label class="ke-field-label">Names</label>
    <span class="ke-field-content">
        <input type="text" name="first-name-query" ng-model="first-name" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" placeholder="First Name" />
    </span>
    <span class="ke-field-content">
        <input type="text" name="middle-name-query" ng-model="first-name" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" placeholder="Middle Name" />
    </span>
    <span class="ke-field-content">
        <input type="text" name="last-name-query" ng-model="last-name" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" placeholder="Last Name" />
    </span>
    <label class="ke-field-label"><input type="checkbox" name="select-mpi-query" ng-model="select-mpi" style="width: 260px" />Search MPI</label>
    <span class="ke-field-content">
        <select name="mpi-query" ng-model="mpi-query" ng-change="onQueryChange()" style="width: 260px" placeholder="Select MPI" >
            <option></option>
            <option value="crossborder-mpi">Cross border MPI</option>
        </select>
    </span>
</form>
