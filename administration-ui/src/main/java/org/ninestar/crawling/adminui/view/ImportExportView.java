package org.ninestar.crawling.adminui.view;

import com.vaadin.ui.TabSheet;
import org.ninestar.crawling.adminui.view.imports.HttpSourceImportExport;
import org.ninestar.crawling.adminui.view.imports.HttpSourceTestImportExport;
import org.ninestar.crawling.adminui.view.imports.NamedQueryImportExport;

import static com.vaadin.server.Sizeable.Unit.PERCENTAGE;

public class ImportExportView extends BaseView {

    public ImportExportView() {
        super("Import / Export");
        TabSheet mainLayout = new TabSheet();
        mainLayout.setWidth(100, PERCENTAGE);
        mainLayout.addTab(new HttpSourceImportExport(), "HTTP Sources");
        mainLayout.addTab(new HttpSourceTestImportExport(), "HTTP Source Tests");
        mainLayout.addTab(new NamedQueryImportExport(), "Named Queries");
        addComponent(mainLayout);
    }




}
