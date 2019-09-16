import { Component, Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class GenericModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    /**
     *
     * @param component Componente de angular que se quiere inyectar dentro del modal
     * @param data entidad y variables necesarias para rellenar el modal
     * @param options opciones del modal, acepta: backdrop, container, keyboard, size, windowClass
     */
    open(component: Component, data: any, options?: any): GenericModal {
        const genericModal: GenericModal = new GenericModal();
        if (!this.isOpen) {
            options = options || { size: 'lg', backdrop: 'static' };
            const modalRef = this.modalService.open(component, options);
            this.isOpen = true;

            Object.keys(data).forEach((datum) => (modalRef.componentInstance[datum] = data[datum]));
            const subject: Subject<any> = new Subject();
            modalRef.result.then(
                (result) => {
                    this.isOpen = false;
                    subject.next(result);
                },
                (reason) => {
                    this.isOpen = false;
                    subject.next(reason);
                }
            );
            genericModal.modalRef = modalRef;
            genericModal.result = subject.asObservable();
        }
        return genericModal;
    }
}

class GenericModal {
    modalRef: NgbModalRef;
    result: Observable<any>;
}
