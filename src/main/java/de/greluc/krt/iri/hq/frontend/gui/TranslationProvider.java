////////////////////////////////////////////////////////////////////////////////
// TimeDb Web                                                                  /
// Copyright (C) 2023 TimeDb Team                                              /
//                                                                             /
// This program is free software: you can redistribute it and/or modify        /
// it under the terms of the GNU General Public License as published by        /
// the Free Software Foundation, either version 3 of the License, or           /
// (at your option) any later version.                                         /
//                                                                             /
// This program is distributed in the hope that it will be useful,             /
// but WITHOUT ANY WARRANTY; without even the implied warranty of              /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the               /
// GNU General Public License for more details.                                /
//                                                                             /
// You should have received a copy of the GNU General Public License           /
// along with this program.  If not, see <http://www.gnu.org/licenses/>.       /
////////////////////////////////////////////////////////////////////////////////

package de.greluc.krt.iri.hq.frontend.gui;

import com.vaadin.flow.i18n.I18NProvider;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import lombok.extern.log4j.Log4j2;

/**
 * Provides methods for the translation.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@pm.me)
 * @version 1.0.1
 * @since 1.0.0
 */
@Log4j2
public class TranslationProvider implements I18NProvider {

  public static final String BUNDLE_PREFIX = "translate";

  public static final Locale LOCALE_DE = Locale.GERMAN;
  public static final Locale LOCALE_DE_DE = Locale.GERMANY;
  public static final Locale LOCALE_EN = Locale.ENGLISH;
  public static final Locale LOCALE_EN_US = Locale.UK;
  public static final Locale LOCALE_EN_UK = Locale.US;

  private final List<Locale> locales = Collections.unmodifiableList(
      Arrays.asList(LOCALE_DE_DE, LOCALE_DE, LOCALE_EN_US, LOCALE_EN_UK, LOCALE_EN));

  /**
   * Returns the {@link java.util.Locale}s that are defined for translations. The first {@link java.util.Locale} is
   * the default {@link java.util.Locale}.
   *
   * @return provided locales
   */
  @Override
  public List<Locale> getProvidedLocales() {
    return locales;
  }

  /**
   * Get the translation for a key with the given {@link java.util.Locale}.
   *
   * <p>Note: For usability and catching missing translations, the implementation
   * should never return a {@code null}, but an exception {@link String} for example '!{key}!'
   *
   * @param key    Translation key.
   * @param locale {@link java.util.Locale} to use.
   * @param params Parameters used in translation string.
   * @return       Translation for key if found otherwise empty {@link String}.
   */
  @Override
  public String getTranslation(String key, Locale locale, Object... params) {
    if (key == null) {
      log.warn("Language request for a null key!");
      return "";
    }

    final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);

    String value;
    try {
      value = bundle.getString(key);
    } catch (final MissingResourceException e) {
      log.warn("Missing resource", e);
      return String.format("!%s: %s", locale.getLanguage(), key);
    }
    if (params.length > 0) {
      value = MessageFormat.format(value, params);
    }
    return value;
  }
}
