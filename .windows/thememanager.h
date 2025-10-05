#ifndef THEMEMANAGER_H
#define THEMEMANAGER_H

#include <QObject>
#include <QString>
#include <QColor>

enum class Theme {
    Light,
    Dark
};

class ThemeManager : public QObject
{
    Q_OBJECT

public:
    static ThemeManager& instance();
    
    Theme currentTheme() const { return m_currentTheme; }
    void setTheme(Theme theme);
    
    // Color getters for current theme
    QColor textColor() const;
    QColor backgroundColor() const;
    QColor foregroundColor() const;
    QColor buttonColor() const;
    QColor outlineColor() const;
    
    // Style sheet generators
    QString getMainWindowStyleSheet() const;
    QString getTabWidgetStyleSheet() const;
    QString getLabelStyleSheet() const;
    QString getPushButtonStyleSheet() const;
    QString getCentralWidgetStyleSheet() const;

signals:
    void themeChanged(Theme theme);

private:
    explicit ThemeManager(QObject *parent = nullptr);
    ThemeManager(const ThemeManager&) = delete;
    ThemeManager& operator=(const ThemeManager&) = delete;
    
    Theme m_currentTheme;
    
    // Color definitions
    struct ThemeColors {
        QColor text;
        QColor background;
        QColor foreground;
        QColor button;
        QColor outline;
    };
    
    ThemeColors m_lightColors;
    ThemeColors m_darkColors;
    
    const ThemeColors& getCurrentColors() const;
};

#endif // THEMEMANAGER_H