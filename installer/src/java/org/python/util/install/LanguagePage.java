package org.python.util.install;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class LanguagePage extends AbstractWizardPage {

    private static final long serialVersionUID = 8048980500023988425L;
    private static Map<Locale, Integer> _languageIndexes = new HashMap<>(2);
    static {
        _languageIndexes.put(Locale.ENGLISH, Integer.valueOf(0));
        _languageIndexes.put(Locale.GERMAN, Integer.valueOf(1));
    }

    private JLabel _label;
    private JComboBox<String> _languageBox;
    private JarInfo _jarInfo;
    private boolean _activated;
    private boolean _stopListening;

    public LanguagePage(JarInfo jarInfo) {
        super();
        _jarInfo = jarInfo;
        _activated = false;
        _stopListening = false;
        initComponents();
    }

    private void initComponents() {
        _label = new JLabel();
        add(_label);
        _languageBox = new JComboBox<>();
        _languageBox.addActionListener(new LanguageBoxListener());
        add(_languageBox);
    }

    protected String getTitle() {
        return getText(WELCOME_TO_JYTHON);
    }

    protected String getDescription() {
        return getText(VERSION_INFO, _jarInfo.getVersion());
    }

    protected boolean isCancelVisible() {
        return true;
    }

    protected boolean isPreviousVisible() {
        return false;
    }

    protected boolean isNextVisible() {
        return true;
    }

    protected JComponent getFocusField() {
        return _languageBox;
    }

    protected void activate() {
        _label.setText(getText(SELECT_LANGUAGE) + ": ");
        // replace combo box items (localization)
        int itemCount = _languageBox.getItemCount();
        _stopListening = true; // adding and removing fires an action event
        for (int i = 0; i < itemCount; i++) {
            _languageBox.removeItemAt(0);
        }
        _languageBox.addItem(getText(ENGLISH)); // keep indexes here
        _languageBox.addItem(getText(GERMAN));
        _stopListening = false;
        if (!_activated) {
            // preselect German if default looks like German
            if (Locale.getDefault().toString().startsWith(Locale.GERMAN.toString())) {
                _languageBox.setSelectedIndex(getLanguageIndex(Locale.GERMAN));
                FrameInstaller.setLanguage(Locale.GERMAN);
            } else {
                FrameInstaller.setLanguage(Locale.ENGLISH);
            }
        } else {
            _languageBox.setSelectedIndex(getLanguageIndex(FrameInstaller.getLanguage()));
        }
        _activated = true;
    }

    protected void passivate() {
    }

    protected void beforeValidate() {
    }

    private int getLanguageIndex(Locale locale) {
        return _languageIndexes.get(locale);
    }

    private Locale getLanguageFromIndex(int index) {
        Integer indexInteger = Integer.valueOf(index);
        for (Entry<Locale, Integer> languageIndex : _languageIndexes.entrySet()) {
            if (languageIndex.getValue().equals(indexInteger)) {
                return languageIndex.getKey();
            }
        }
        return null;
    }

    private class LanguageBoxListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if (!_stopListening) {
                FrameInstaller.setLanguage(getLanguageFromIndex(_languageBox.getSelectedIndex()));
            }
        }
    }

}