import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TeammatesCommonModule } from '../teammates-common.module';
import { ViewPhotoPopoverComponent } from './view-photo-popover.component';

describe('ViewPhotoPopoverComponent', () => {
  let component: ViewPhotoPopoverComponent;
  let fixture: ComponentFixture<ViewPhotoPopoverComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [TeammatesCommonModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewPhotoPopoverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
