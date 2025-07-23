#include "mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , m_central(new QWidget(this))
    , m_vbox(new QVBoxLayout)
    , m_button(new QPushButton(tr("Generate"), this))
    , m_tabs(new QTabWidget(this))
    , m_gridInv(new QGridLayout)
    , m_gridPeggle(new QGridLayout)
    , m_gridBoss(new QGridLayout)
{
    setupNameMaps();

    // Build UI
    m_vbox->addWidget(m_button, 0, Qt::AlignTop);
    m_vbox->addWidget(m_tabs, 1);

    auto *pageInv    = new QWidget; pageInv->setLayout(m_gridInv);
    auto *pagePeggle = new QWidget; pagePeggle->setLayout(m_gridPeggle);
    auto *pageBoss   = new QWidget; pageBoss->setLayout(m_gridBoss);

    m_tabs->addTab(pageInv,    tr("Inventory"));
    m_tabs->addTab(pagePeggle, tr("Peggle Levels"));
    m_tabs->addTab(pageBoss,   tr("Boss Level"));

    m_central->setLayout(m_vbox);
    setCentralWidget(m_central);
    setWindowTitle(tr("Inventory / Peggle / Boss Generator"));
    resize(600, 300);

    connect(m_button, &QPushButton::clicked,
            this, &MainWindow::onGenerateClicked);
}

MainWindow::~MainWindow() = default;

void MainWindow::setupNameMaps()
{
    invNames = {
        {}, "Bjorn", "Jimmy Lighting", "Kat Tut", "Spork",
        "Caddle", "Reinfield", "Tula", "Magic Hat",
        "Nice Green Dragon", "Master Xu"
    };

    levelNames = { {} };
    levelNames << "Peggleland" << "Slip and Slide" << "Bjorn's Gazebo"
               /* … up through 50 entries … */
               << "Yang, Yin" << "Zen Frog";

    bossNames.clear();
    bossNames.resize(56);   // indices 0..55
    bossNames[51] = "Paw Reader";
    bossNames[52] = "End of Time";
    bossNames[53] = "Billions & Billions";
    bossNames[54] = "Don't Panic";
    bossNames[55] = "Beyond Reason";
}

void MainWindow::clearLayout(QGridLayout *lay)
{
    while (auto item = lay->takeAt(0)) {
        if (auto w = item->widget())
            w->deleteLater();
        delete item;
    }
}

void MainWindow::onGenerateClicked()
{
    clearLayout(m_gridInv);
    clearLayout(m_gridPeggle);
    clearLayout(m_gridBoss);

    // 1) Inventory (3×1), values 1–10
    for (int i = 0; i < 3; ++i) {
        int n = QRandomGenerator::global()->bounded(1, 11);
        // format: "01 Bjorn"
        QString txt = QStringLiteral("%1 %2")
                          .arg(n, 2, 10, QChar('0'))
                          .arg(invNames[n]);
        auto *lbl = new QLabel(txt);
        lbl->setAlignment(Qt::AlignCenter);
        m_gridInv->addWidget(lbl, 0, i);
    }

    // 2) Peggle Levels (8×2, 15 cells)
    for (int i = 0; i < 15; ++i) {
        int d1 = QRandomGenerator::global()->bounded(1, 51);
        int d2 = QRandomGenerator::global()->bounded(1, 11);
        QString txt = QStringLiteral("%1 %2, %3 %4")
                          .arg(d1, 2, 10, QChar('0'))
                          .arg(levelNames[d1])
                          .arg(d2, 2, 10, QChar('0'))
                          .arg(invNames[d2]);
        auto *lbl = new QLabel(txt);
        lbl->setAlignment(Qt::AlignCenter);
        int row = i / 8, col = i % 8;
        m_gridPeggle->addWidget(lbl, row, col);
    }

    // 3) Boss Level (1×1)
    int d1 = QRandomGenerator::global()->bounded(51, 56);
    int d2 = QRandomGenerator::global()->bounded(1, 11);
    {
        QString txt = QStringLiteral("%1 %2, %3 %4")
                          .arg(d1, 2, 10, QChar('0'))
                          .arg(bossNames[d1])
                          .arg(d2, 2, 10, QChar('0'))
                          .arg(invNames[d2]);
        auto *lbl = new QLabel(txt);
        lbl->setAlignment(Qt::AlignCenter);
        m_gridBoss->addWidget(lbl, 0, 0);
    }
}