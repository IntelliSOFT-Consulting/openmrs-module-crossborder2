<%
    ui.decorateWith("kenyaui", "panel", [ heading: "Search for a Patient" ])

    ui.includeJavascript("crossborder2", "kenyaemr/controllers/patient.js")

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
        <input type="text" name="cb-id-query" ng-model="cbIdQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" />
    </span>
   <label class="ke-field-label">Patient clinic number</label>
    <span class="ke-field-content">
        <input type="text" name="clinic-no-query" ng-model="clinicNoQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" />
    </span>
    <label class="ke-field-label">Names</label>
    <span class="ke-field-content">
        <input type="text" name="first-name-query" ng-model="firstNameQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" placeholder="First Name" />
    </span>
    <span class="ke-field-content">
        <input type="text" name="middle-name-query" ng-model="middleNameQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" placeholder="Middle Name" />
    </span>
    <span class="ke-field-content">
        <input type="text" name="last-name-query" ng-model="lastNameQuery" ng-enter="updateSearch()" ng-change="onQueryChange()" style="width: 260px" placeholder="Last Name" />
    </span>
    <label class="ke-field-label"><input type="checkbox" name="select-mpi" ng-model="selectMpiQuery" ng-change="onQueryChange()" style="width: 260px" />Search MPI</label>
    <span class="ke-field-content">
        <select name="mpi-query" ng-model="mpiQuery" ng-change="onQueryChange()" style="width: 260px" placeholder="Select MPI" >
            <option></option>
            <option value="crossborder-mpi">Cross border MPI</option>
        </select>
    </span>
</form>
