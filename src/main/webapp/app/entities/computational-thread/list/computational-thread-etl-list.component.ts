import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Etl, EtlBase, EtlService, ExecutionPlatform } from '../../etl/';
import { ResponseWrapper } from '../../../shared';
import { ComputationalThreadsEtl } from '../computational-threads-etl-model';
import { ExternalItem } from '../../external-item/external-item.model';

@Component({
    selector: 'ac-computational-thread-etl-list',
    templateUrl: 'computational-thread-etl-list.component.html',
    styleUrls: ['computational-thread-etl-list.component.scss']
})
export class ComputationalThreadEtlListComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'ComputationalThreadEtlList';

    @Input() idThread: number;
    @Input() etlsThread: ComputationalThreadsEtl[];

    @Input() etlList: Etl[];

    @Input() selectedEtls: Etl[];
    @Output() selectedEtlsChange = new EventEmitter<Etl[]>();

    @Input() isEdit: boolean;

    @Input() selectedStatistical: ExternalItem;

    public query = '';

    public propertiesToQuery;
    public itemTemplate: Function;

    constructor(private etlService: EtlService) {}

    ngOnInit() {
        this.loadAll(this.selectedStatistical.code);
    }

    ngOnDestroy() {}

    public loadAll(code: string) {
        this.etlService
            .query({
                query: this.query,
                includeDeleted: false,
                lastExecution: '',
                lastExecutionByResult: '',
                executionPlatform: ExecutionPlatform.APACHE_HOP,
                restriction: code
            })
            .subscribe((res: ResponseWrapper) => this.onSuccess(res.json));
    }

    private onSuccess(data: EtlBase[]) {
        this.etlList = this.etlBaseToEtl(data);
        this.initSelectedEtls();
    }

    private initSelectedEtls() {
        this.etlsThread.forEach((element, index) => {
            let tmp = null;
            if ((tmp = this.etlList.find((etl) => etl.id == element.etl.id))) {
                this.selectedEtls[element.executionOrder] = tmp;
            }
        });
    }

    onChange() {
        this.selectedEtlsChange.emit(this.selectedEtls);
    }

    private etlBaseToEtl(data: EtlBase[]) {
        return data.map((etlBase) => this.setEtl(etlBase));
    }

    private setEtl(data: EtlBase) {
        const etl: Etl = new Etl();
        etl.id = data.id;
        etl.code = data.code;
        etl.name = data.name;
        etl.organizationInCharge = data.organizationInCharge;
        etl.type = data.type;
        etl.executionPlanning = data.executionPlanning;
        etl.externalItem = data.externalItem;
        etl.nextExecution = data.nextExecution;
        etl.lastExecution = data.lastExecution;
        etl.executionPlatform = data.executionPlatform;
        return etl;
    }

    public hasEtls() {
        return !this.selectedEtls || this.selectedEtls.length === 0;
    }
}
