#include "thememanager.h"

ThemeManager& ThemeManager::instance()
{
    static ThemeManager instance;
    return instance;
}

ThemeManager::ThemeManager(QObject *parent)
    : QObject(parent)
    , m_currentTheme(Theme::Light)
{
    // Initialize light theme colors
    m_lightColors.text = QColor("#000000");       // Black text
    m_lightColors.background = QColor("#d3f0fc"); // Light blue background
    m_lightColors.foreground = QColor("#178af7"); // Blue foreground
    m_lightColors.button = QColor("#da070f");     // Red button
    m_lightColors.outline = QColor("#000000");    // Black outline

    // Initialize dark theme colors
    m_darkColors.text = QColor("#FFFFFF");        // White text
    m_darkColors.background = QColor("#252525");  // Dark gray background
    m_darkColors.foreground = QColor("#4a759e");  // Blue-gray foreground
    m_darkColors.button = QColor("#294891");      // Dark blue button
    m_darkColors.outline = QColor("#FFFFFF");     // White outline
}

void ThemeManager::setTheme(Theme theme)
{
    if (m_currentTheme != theme) {
        m_currentTheme = theme;
        emit themeChanged(theme);
    }
}

QColor ThemeManager::textColor() const
{
    return getCurrentColors().text;
}

QColor ThemeManager::backgroundColor() const
{
    return getCurrentColors().background;
}

QColor ThemeManager::foregroundColor() const
{
    return getCurrentColors().foreground;
}

QColor ThemeManager::buttonColor() const
{
    return getCurrentColors().button;
}

QColor ThemeManager::outlineColor() const
{
    return getCurrentColors().outline;
}

const ThemeManager::ThemeColors& ThemeManager::getCurrentColors() const
{
    return (m_currentTheme == Theme::Light) ? m_lightColors : m_darkColors;
}

QString ThemeManager::getMainWindowStyleSheet() const
{
    const auto& colors = getCurrentColors();
    return QString(
        "QMainWindow {"
        "  background-color: %1;"
        "  color: %2;"
        "}")
        .arg(colors.background.name())
        .arg(colors.text.name());
}

QString ThemeManager::getCentralWidgetStyleSheet() const
{
    const auto& colors = getCurrentColors();
    return QString(
        "QWidget {"
        "  background-color: %1;"
        "  color: %2;"
        "}")
        .arg(colors.background.name())
        .arg(colors.text.name());
}

QString ThemeManager::getTabWidgetStyleSheet() const
{
    const auto& colors = getCurrentColors();
    return QString(
        "QTabWidget::pane {"
        "  border: 6px solid %1;"
        "  background-color: %2;"
        "}"
        "QTabWidget::tab-bar {"
        "  alignment: left;"
        "}"
        "QTabBar::tab {"
        "  background-color: %3;"
        "  color: %4;"
        "  border: 6px solid %1;"
        "  border-bottom: none;"
        "  border-top-left-radius: 10px;"
        "  border-top-right-radius: 10px;"
        "  padding: 8px 16px;"
        "  margin-right: 2px;"
        "  min-width: 80px;"
        "}"
        "QTabBar::tab:selected {"
        "  background-color: %2;"
        "  border-bottom: 6px solid %2;"
        "}"
        "QTabBar::tab:hover {"
        "  background-color: %5;"
        "}")
        .arg(colors.outline.name())
        .arg(colors.background.name())
        .arg(colors.foreground.name())
        .arg(colors.text.name())
        .arg(colors.button.name());
}

QString ThemeManager::getLabelStyleSheet() const
{
    const auto& colors = getCurrentColors();
    return QString(
        "QLabel {"
        "  background-color: %1;"
        "  color: %2;"
        "  border: 6px solid %3;"
        "  border-radius: 8px;"
        "  padding: 12px;"
        "  margin: 4px;"
        "  font-weight: 500;"
        "  font-size: 11pt;"
        "}")
        .arg(colors.foreground.name())
        .arg(colors.text.name())
        .arg(colors.outline.name());
}

QString ThemeManager::getPushButtonStyleSheet() const
{
    const auto& colors = getCurrentColors();
    return QString(
        "QPushButton {"
        "  background-color: %1;"
        "  color: %2;"
        "  border: 6px solid %3;"
        "  border-radius: 12px;"
        "  padding: 12px 24px;"
        "  margin: 8px;"
        "  font-weight: bold;"
        "  font-size: 12pt;"
        "  text-align: center;"
        "}"
        "QPushButton:hover {"
        "  background-color: %4;"
        "  transform: scale(1.05);"
        "}"
        "QPushButton:pressed {"
        "  background-color: %5;"
        "  border-width: 4px;"
        "}")
        .arg(colors.button.name())
        .arg(colors.text.name())
        .arg(colors.outline.name())
        .arg(colors.foreground.name())
        .arg(colors.background.name());
}