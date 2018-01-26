import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { Autosize } from 'ng-autosize';

@Component({
    selector: 'jhi-textarea-form',
    templateUrl: './textarea-form.component.html'
})
 export class TextAreaFormComponent implements AfterViewInit {

    public texto: String;
    public editMode = true;

    @ViewChild(Autosize)
    displayContainer: Autosize;

    constructor() {
        this.texto = `Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla fringilla urna in massa euismod, nec semper lacus varius. Nulla nisl dui, porttitor eget quam vitae, placerat consectetur turpis. Praesent enim erat, ornare nec aliquam a, rutrum nec sapien. Aliquam id ex elit. Proin sodales justo id sem vestibulum, vitae mollis arcu tincidunt. Vestibulum et commodo velit, et finibus turpis. Duis ultricies leo nec malesuada aliquet. Pellentesque quis justo vel libero viverra dictum vel ac nunc. Praesent eu tincidunt magna, accumsan rutrum felis. Morbi non vulputate enim.
        Fusce sit amet efficitur sapien. Integer sem justo, sodales id lectus et, elementum molestie leo. Suspendisse potenti. Cras magna dolor, tempor a nulla eget, ullamcorper maximus risus. Curabitur eu magna erat. Donec ultrices odio quis quam fringilla fringilla. Nulla non arcu id nisi fermentum vestibulum. Nam mollis tortor mollis, pellentesque turpis et, egestas lorem.
        Nullam ultrices tellus purus, ac egestas mi molestie nec. Praesent cursus mauris eget nisi pretium, vitae tincidunt turpis accumsan. Nullam nisi ligula, condimentum at dictum eu, sollicitudin vel nulla. Aliquam ut lacus efficitur ligula faucibus convallis in non nulla. Donec nec efficitur ante. Duis nec nulla in risus dictum viverra. Phasellus ac dignissim mi. Ut ac sagittis purus, et consequat urna. Vivamus quis nibh eget lectus volutpat porta nec in sem. Donec porta, tortor nec dignissim aliquet, eros dui cursus felis, sit amet porta ante dui non sapien. Proin finibus velit id massa viverra, ac scelerisque nibh pulvinar. Etiam bibendum vestibulum eros id congue. Quisque consequat odio elit, a viverra libero fermentum nec. Suspendisse ultrices ac mi ut convallis.`;
    }

    ngAfterViewInit() {
        setTimeout(() => this.displayContainer.adjust());
    }

    public toggleEditMode(): void {
        this.editMode = !this.editMode;
    }
}
