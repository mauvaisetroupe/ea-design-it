<template>
  <div>
    <h2 id="page-heading" data-cy="capabilityImportHeading">
      <span id="capability-import-heading">Capabilities Imports</span>
    </h2>

    UPDATED
    <div v-if="!rowsLoaded">
      <div class="form-group">
        <div class="custom-file">
          <input type="file" class="custom-file-input" id="customFile" @change="handleFileUpload($event)" :disabled="fileSubmited" />
          <label class="custom-file-label" for="customFile">{{ excelFileName }}</label>
        </div>
      </div>
      <div class="form-group" v-if="excelFile && !analysisDone">
        <button type="submit" class="btn btn-primary mb-2" v-on:click="submitFileForAnalysis()" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>  Submit File
        </button>
      </div>
      <div class="form-group" v-if="analysisDone && somethingToImport">
        <button type="submit" class="btn btn-primary mb-2" v-on:click="confirmUploadedFile()">Confirm Import</button>
      </div>
    </div>

    <div v-if="analysisDone"> 

      <div v-if="!somethingToImport" class="alert alert-warning">
          <span>Nothing to import</span>
     </div>

      <div v-if="capabilitiesImportAnalysis.capabilitiesToAdd && capabilitiesImportAnalysis.capabilitiesToAdd.length">
        <h3>Capabilities To Add</h3>
        <div>
          <table class="table table-striped">
            <tbody>
              <tr v-for="capabilityAction in capabilitiesImportAnalysis.capabilitiesToAdd" :key="capabilityAction.capability.id">
                <td>
                  <CapabilityTreeComponent :capability="capabilityAction.capability"/>
                </td>
                <td>
                  <div class="d-flex justify-content-end">
                    <b-form-group
                      v-slot="{ ariaDescribedby }"
                    >
                      <b-form-radio-group
                        id="btn-radios-1"
                        :options="toAddOption"
                        :aria-describedby="ariaDescribedby"
                        size="sm"
                        button-variant="outline-success"
                        buttons
                        v-model="capabilityAction.action"
                      ></b-form-radio-group>
                    </b-form-group> 
                  </div> 
                </td>
              </tr>
            </tbody>  
          </table>

        </div>
      </div>
  
      <div v-if="capabilitiesImportAnalysis.capabilitiesToDelete && capabilitiesImportAnalysis.capabilitiesToDelete.length">
        <h3>Capabilities To Delete</h3>
        <div>
          <table class="table table-striped">
            <tbody>
              <tr>
                <td colspan="2">
                  <div class="d-flex justify-content-end">
                    <b-form-group v-slot="{ ariaDescribedby }">
                      <button class="btn btn-danger m-0 p-1">Delete</button><button class="btn btn-danger m-0 p-1" @click="ignoreAllDelete()">Ignore All</button>
                    </b-form-group> 
                  </div>    
                </td>
              </tr>
              <tr v-for="capabilityAction in capabilitiesImportAnalysis.capabilitiesToDelete" :key="capabilityAction.capability.id">
                <td>
                  <CapabilityTreeComponent :capability="capabilityAction.capability"/>
                </td> 
                <td>
                  <div class="d-flex justify-content-end">
                    <b-form-group
                      v-slot="{ ariaDescribedby }"
                    >
                      <b-form-radio-group
                        id="btn-radios-2"
                        :options="toDeleteOption"
                        :aria-describedby="ariaDescribedby"
                        size="sm"
                        button-variant="outline-danger"
                        buttons
                        v-model="capabilityAction.action"
                      ></b-form-radio-group>
                    </b-form-group> 
                  </div> 
                </td>
              </tr>         
            </tbody>  
          </table>
        </div>
      </div>


      <div v-if="capabilitiesImportAnalysis.capabilitiesToDeleteWithMappings && capabilitiesImportAnalysis.capabilitiesToDeleteWithMappings.length">
        <h3>Capabilities To Delete (with associated mapping)</h3>
        <div>
          <table class="table table-striped">
            <tbody>
              <tr v-for="capabilityAction in capabilitiesImportAnalysis.capabilitiesToDeleteWithMappings" :key="capabilityAction.capability.id">
                <td>
                  <CapabilityTreeComponent :capability="capabilityAction.capability"/>
                </td>  
                <td>
                  <div class="d-flex justify-content-end">
                    <b-form-group
                      v-slot="{ ariaDescribedby }"
                    >
                      <b-form-radio-group
                        id="btn-radios-3"
                        :options="toDeleteWithMappingOption"
                        :aria-describedby="ariaDescribedby"
                        size="sm"
                        button-variant="outline-danger"
                        buttons
                        v-model="capabilityAction.action"
                      ></b-form-radio-group>
                    </b-form-group> 
                  </div> 
                </td>
              </tr>
            </tbody>  
          </table>
        </div>
      </div>

      <div v-if="capabilitiesImportAnalysis.ancestorsOfCapabilitiesWithMappings && capabilitiesImportAnalysis.ancestorsOfCapabilitiesWithMappings.length">
        <h3>Capabilities To Delete (with child with Mappimg)</h3>
        <div>
          <table class="table table-striped">
            <tbody>
              <tr v-for="capabilityAction in capabilitiesImportAnalysis.capabilitiesToDeleteWithMappings" :key="capabilityAction.capability.id">
                <td>
                  <CapabilityTreeComponent :capability="capabilityAction.capability"/>
                </td>  
                <td>
                  <div class="d-flex justify-content-end">
                    <b-form-group
                      v-slot="{ ariaDescribedby }"
                    >
                      <b-form-radio-group
                        id="btn-radios-3"
                        :options="toDeleteWithChildMappingOption"
                        :aria-describedby="ariaDescribedby"
                        size="sm"
                        button-variant="outline-danger"
                        buttons
                        v-model="capabilityAction.action"
                      ></b-form-radio-group>
                    </b-form-group> 
                  </div> 
                </td>
              </tr>
            </tbody>  
          </table>
        </div>
      </div>      

      <div v-if="capabilitiesImportAnalysis.capabilitiesToDeleteWithMappings && capabilitiesImportAnalysis.capabilitiesToDeleteWithMappings.length">
        <h3>Error lines</h3>
        <div>
          <table class="table table-striped">
            <tbody>
              <tr v-for="(error, i) in capabilitiesImportAnalysis.errorLines" :key="i">
                <td>
                  {{ error }}
                </td>  
                <td>
                  <div class="d-flex justify-content-end">
                    <b-form-group
                      v-slot="{ ariaDescribedby }"
                    >
                      <b-form-radio-group
                        id="btn-radios-4"
                        :options="[{ text: 'Ignore', value: 'Ignore'}]"
                        :aria-describedby="ariaDescribedby"
                        size="sm"
                        button-variant="outline-danger  active"
                        buttons
                      ></b-form-radio-group>
                    </b-form-group> 
                  </div> 
                </td>
              </tr>
            </tbody>  
          </table>
        </div>
      </div>

    </div>
    <div class="table-responsive" v-if="capabilitiesImports && capabilitiesImports.length > 0">
      <table class="table table-striped" aria-describedby="capabilitiesImports">
        <thead>
          <tr>
            <th scope="row"><span>Status</span></th>

            <th scope="row"><span>Error</span></th>

            <th scope="row"><span>Domain</span></th>

            <th scope="row"><span>L0 Level</span></th>
            <th scope="row"><span>L0 Name</span></th>
            <th scope="row"><span>L0 Description</span></th>

            <th scope="row"><span>L1 Level</span></th>
            <th scope="row"><span>L1 Name</span></th>
            <th scope="row"><span>L1 Description</span></th>

            <th scope="row"><span>L2 Level</span></th>
            <th scope="row"><span>L2 Name</span></th>
            <th scope="row"><span>L2 Description</span></th>

            <th scope="row"><span>L3 Level</span></th>
            <th scope="row"><span>L3 Name</span></th>
            <th scope="row"><span>L3 Description</span></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(dto, i) in filteredCapabilitiesImports" :key="i">
            <td>
              <span v-bind:class="[dto.status === 'ERROR' ? 'rederror' : '']">{{ dto.status }} </span>
            </td>
            <td>{{ dto.error }}</td>
            <td>
              <span v-if="dto.domain">{{ dto.domain.name }}</span>
            </td>
            <td>
              <span v-if="dto.l0">{{ dto.l0.level }}</span>
            </td>
            <td>
              <span v-if="dto.l0">{{ dto.l0.name }}</span>
            </td>
            <td>
              <span v-if="dto.l0">{{ dto.l0.description }}</span>
            </td>

            <td>
              <span v-if="dto.l1">{{ dto.l1.level }}</span>
            </td>
            <td>
              <span v-if="dto.l1">{{ dto.l1.name }}</span>
            </td>
            <td>
              <span v-if="dto.l1">{{ dto.l1.description }}</span>
            </td>

            <td>
              <span v-if="dto.l2">{{ dto.l2.level }}</span>
            </td>
            <td>
              <span v-if="dto.l2">{{ dto.l2.name }}</span>
            </td>
            <td>
              <span v-if="dto.l2">{{ dto.l2.description }}</span>
            </td>

            <td>
              <span v-if="dto.l3">{{ dto.l3.level }}</span>
            </td>
            <td>
              <span v-if="dto.l3">{{ dto.l3.name }}</span>
            </td>
            <td>
              <span v-if="dto.l3">{{ dto.l3.description }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./capability-import-upload-file.component.ts"></script>
<style>
.rederror {
  background-color: red;
  color: white;
}
</style>
