package org.ninestar.crawling.adminui.view.namedquery;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.ninestar.crawling.data.DataUtils;
import org.ninestar.crawling.data.HttpArticle;
import org.ninestar.crawling.data.PageableList;

public class NamedQueryResultsPanel extends Panel {

    public NamedQueryResultsPanel(PageableList<HttpArticle> results) {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Label countLabel = new Label(String.format("%s documents matched", results.getTotalCount()));
        countLabel.addStyleName(ValoTheme.LABEL_LARGE);
        countLabel.setSizeFull();
        layout.addComponent(countLabel);

        for (HttpArticle article : results.getItems()) {
            String labelHtml = String.format("%s&nbsp;<a href=\"%s\" target=\"_blank\">%s</a> - <strong>%s</strong>",
                    DataUtils.formatInUTC(article.getPublished()), article.getUrl(), article.getTitle(), article.getSource());
            Label articleLabel = new Label(labelHtml);
            articleLabel.setContentMode(ContentMode.HTML);
            articleLabel.setSizeFull();
            layout.addComponent(articleLabel);
        }
        setContent(layout);
    }
}
