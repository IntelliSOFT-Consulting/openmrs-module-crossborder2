kenyaemrApp.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});

kenyaemrApp.service('PatientService', function ($rootScope) {

    /**
     * Broadcasts new patient search parameters
     */
    this.updateSearch = function(which, cbIdQuery, clinicNoQuery, nameQuery, genderQuery, selectMpiQuery, mpiQuery) {

        $rootScope.$broadcast('patient-search', { which: which , cbIdQuery: cbIdQuery, clinicNoQuery: clinicNoQuery, nameQuery: nameQuery, genderQuery: genderQuery, selectMpiQuery: selectMpiQuery, mpiQuery: mpiQuery});
    };
});

/**
 * Controller for patient search form
 */
kenyaemrApp.controller('AdvancedPatientSearchForm', ['$scope', 'PatientService','$timeout', function($scope, patientService, $timeout) {

    $scope.query = [];

    $scope.init = function(which) {
        $scope.which = which;
        $scope.$evalAsync($scope.updateSearch); // initiate an initial search
    };
    $scope.delayOnChange = (function() {
        var promise = null;
        return function(callback, ms) {
            $timeout.cancel(promise); //clearTimeout(timer);
            promise = $timeout(callback, ms); //timer = setTimeout(callback, ms);
        };
    })();

    $scope.onQueryChange = function() {
        if($scope.query === '') {
            $scope.updateSearch();
        }
    };

    $scope.updateSearch = function() {
        patientService.updateSearch($scope.which, $scope.cbIdQuery, $scope.clinicNoQuery, $scope.nameQuery, $scope.genderQuery, $scope.selectMpiQuery, $scope.mpiQuery);
    };
}]);

/**
 * Controller for patient search results
 */
kenyaemrApp.controller('AdvancedPatientSearchResults', ['$scope', '$http', function($scope, $http) {

    $scope.query = [];
    $scope.results = [];
    $scope.showLoader = false;
    $scope.checkedInSelected = false;
    $scope.allSelected = false;

    /**
     * Initializes the controller
     * @param appId the current app id
     * @param which
     */
    $scope.init = function(appId, pageProvider, page) {
        $scope.appId = appId;
        $scope.pageProvider = pageProvider;
        $scope.page = page;
    };

    /**
     * Listens for the 'patient-search' event
     */
    $scope.$on('patient-search', function(event, data) {
        $scope.which = data.which;
        $scope.clinicNoQuery = data.clinicNoQuery;
        $scope.cbIdQuery = data.cbIdQuery;
        $scope.nameQuery = data.nameQuery;
        $scope.genderQuery = data.genderQuery;
        $scope.mpiQuery = data.mpiQuery;
        $scope.selectMpiQuery = data.selectMpiQuery;

        $scope.showLoader = true;

        $scope.refresh();
    });

    /**
     * Refreshes the person search
     */
    $scope.refresh = function() {
        if(!$scope.query && $scope.which === "checked-in") {
            $scope.allSelected= false;
            $scope.results = [];
            $scope.showLoader = false;
        }

        $scope.query = [];

        if ($scope.cbIdQuery) {
            $scope.query.push({"cbId": $scope.cbIdQuery});
        }
        if ($scope.clinicNoQuery) {
            $scope.query.push({"clinicNo": $scope.clinicNoQuery});
        }

        if ($scope.nameQuery) {
            $scope.query.push({"name": $scope.nameQuery});
        }
        if ($scope.genderQuery) {
            $scope.query.push({"gender": $scope.genderQuery});
        }
        if ($scope.selectMpiQuery) {
            $scope.query.push({"selectMpi": $scope.selectMpiQuery});
        }
        if ($scope.mpiQuery) {
            $scope.query.push({"mpi": $scope.mpiQuery});
        }

       if($scope.query && $scope.which === "checked-in") {
            $scope.crossborder = false;
            $scope.checkedInSelected = true;
            $scope.allSelected = false;
            $http.get(ui.fragmentActionLink('kenyaemr', 'search', 'patients', { appId: $scope.appId, q: $scope.query, which: $scope.which })).
            success(function(data) {
                if($scope.checkedInSelected) {
                    $scope.results =  data;
                    $scope.showLoader = false;
                }
            });
        }

        if(!$scope.query && $scope.which === "all") {
            $scope.crossborder = false;
            $scope.checkedInSelected= false;
            $scope.results = [];
            $scope.showLoader = false;
        }

        /*
        if($scope.query && $scope.which === "all") {
            $scope.crossborder = false;
            $scope.checkedInSelected = false;
            $scope.allSelected = true;
            $http.get(ui.fragmentActionLink('kenyaemr', 'search', 'patients', { appId: $scope.appId, q: $scope.query, which: $scope.which })).
            success(function(data) {
                if($scope.allSelected) {
                    $scope.results =  data;
                    $scope.showLoader = false;
                }
            });
        } */

        if($scope.query) {
            $scope.crossborder = true;
            $scope.checkedInSelected = false;
            $scope.allSelected = false;
            $scope.searchObject = [];

            $http.get(ui.fragmentActionLink('crossborder2', 'advancedSearch', 'patients', { appId: $scope.appId, q: JSON.stringify($scope.query), which: $scope.which })).
            success(function(data) {
                if($scope.crossborder) {
                    $scope.results =  data;
                    $scope.showLoader = false;
                }
            });
        }

    };

    /**
     * Result click event handler
     * @param patient the clicked patient
     */
    $scope.onResultClick = function(patient) {
        if (patient.openmrsId !== null && patient.openmrsId === "") {
            ui.navigate("crossborder2", "kenyaemr/patient/cbPatientRegistration", { personId: patient.id });
        } else {
            if (patient.crossBorderId !== null && patient.crossBorderId !== "") {
                if (confirm("This record has been retrieved from the Regional MPI, confirm that you want to create a new record based on this information")) {
                    ui.navigate("crossborder2", "kenyaemr/patient/cbPatientRegistration", { personId: patient.id, crossBorderId: patient.crossBorderId });
                }
            }
        }
    };

    /**
     * Result click event handler
     * @param patient the clicked patient
     */
    $scope.onCrossBorderResultClick = function(patient) {
        ui.navigate($scope.pageProvider, $scope.page, { patientId: patient.id });
    };

}]);

/**
 * Controller for similar patients (on registration form)
 */
kenyaemrApp.controller('SimilarPatients', ['$scope', '$http', function($scope, $http) {

    $scope.givenName = '';
    $scope.familyName = '';
    $scope.results = [];
    $scope.showLoader = false;

    /**
     * Initializes the controller
     * @param appId the current app id
     * @param which
     */
    $scope.init = function(appId, pageProvider, page) {
        $scope.appId = appId;
        $scope.pageProvider = pageProvider;
        $scope.page = page;
        $scope.refresh();
    };

    /**
     * Refreshes the patient search
     */
    $scope.refresh = function() {
        var query = $scope.givenName + ' ' + $scope.familyName;
        $http.get(ui.fragmentActionLink('kenyaemr', 'search', 'patients', { appId: $scope.appId, q: query, which: 'all' })).
        success(function(data) {
            $scope.results = data;
            $scope.showLoader = false;
        });
    };

}]);
