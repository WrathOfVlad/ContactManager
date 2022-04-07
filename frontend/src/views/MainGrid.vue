<template>
    <ag-grid-vue
        style="width: 500px; height: 200px"
        class="ag-theme-alpine"
        @grid-ready="onGridReady"
        :columnDefs="columnDefs"
        :rowData="rowData"
    >
  </ag-grid-vue>
  <button @click="loadData()">Load Data</button>
</template>

<script>
    import "ag-grid-community/dist/styles/ag-grid.css";
    import "ag-grid-community/dist/styles/ag-theme-alpine.css";
    import { AgGridVue } from "ag-grid-vue3";
    
    export default {
        name:"agGrid",
        components:{
            AgGridVue
        },
        setup(){

            let rowData = [];
            let gridApi;

            let columnDefs = [
                { headerName: "Make", field: "make",sortable:true },
                { headerName: "Model", field: "model", sortable:true},
                { headerName: "Price", field: "price", sortable: true },
            ];
            
            const loadData = () => {
                $.ajax({
                    url: "http://localhost:8080/api/contacts",
                    success: result => {
                        gridApi.setRowData(result);
                        console.log(result);
                    },
                    error: error => {
                        console.error(error);
                    }
                });
            };

            const onGridReady = function(params) {
                gridApi = params.api;
            }
            
            return {
                columnDefs,
                rowData,
                loadData,
                onGridReady
            };
        }
    }
</script>