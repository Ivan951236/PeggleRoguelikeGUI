#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QVBoxLayout>
#include <QPushButton>
#include <QTabWidget>
#include <QGridLayout>
#include <QRandomGenerator>
#include <QVector>
#include <QString>
#include <QLabel>

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = nullptr);
    ~MainWindow() override;

private slots:
    void onGenerateClicked();

private:
    void setupNameMaps();
    void clearLayout(QGridLayout *lay);

    QWidget*      m_central;
    QVBoxLayout*  m_vbox;
    QPushButton*  m_button;
    QTabWidget*   m_tabs;
    QGridLayout*  m_gridInv;
    QGridLayout*  m_gridPeggle;
    QGridLayout*  m_gridBoss;

    QVector<QString> invNames;     // 1–10
    QVector<QString> levelNames;   // 1–50
    QVector<QString> bossNames;    // indices 0–55
};

#endif // MAINWINDOW_H