import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ComputationalThreads } from '../computational-thread.model';
import { EtlBase, EtlService } from '../../etl';
import { ResponseWrapper } from '../../../shared';
import { EmbeddedTemplateAst } from '@angular/compiler';

@Component({
    selector: 'ac-computational-thread-etl-list',
    templateUrl: 'computational-thread-etl-list.component.html',
    styleUrls: ['computational-thread-etl-list.component.scss']
})
export class ComputationalThreadEtlListComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'ComputationalThreadEtlList';

    @Input() idThread: number;

    public etlList: EtlBase[];
    public etlListv2: EtlBase[];
    public selectedEtls: EtlBase[];
    selectAll = false;
    public query: string = '';

    public computationalThreads: ComputationalThreads[];

    public propertiesToQuery;
    public itemTemplate: Function;

    constructor(private etlService: EtlService) {}

    ngOnInit() {
        this.loadAll();
    }

    ngOnDestroy() {}

    private loadAll() {
        this.etlService
            .query({
                query: this.query,
                includeDeleted: false,
                lastExecution: '',
                lastExecutionByResult: ''
            })
            .subscribe((res: ResponseWrapper) => this.onSuccess(res.json));
    }

    private onSuccess(data: EtlBase[]) {
        this.etlList = data;
        let final = [];
        data.forEach((element, index) => {
            final.push({ name: element.name, code: element.code });
        });
        this.etlListv2 = final;
        console.log(this.etlListv2);
    }

    onChange(etlListtmp: any) {
        let idList: number[] = [];
        console.log(etlListtmp.value);
        etlListtmp.value.forEach((element, index) => {
            idList.push(element.id);
        });
        console.log(idList);
    }
}
