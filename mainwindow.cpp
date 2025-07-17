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
    // 1) Prepare name maps
    setupNameMaps();

    // 2) Build UI
    m_vbox->addWidget(m_button, 0, Qt::AlignTop);
    m_vbox->addWidget(m_tabs, 1);

    QWidget* pageInv    = new QWidget; pageInv->setLayout(m_gridInv);
    QWidget* pagePeggle = new QWidget; pagePeggle->setLayout(m_gridPeggle);
    QWidget* pageBoss   = new QWidget; pageBoss->setLayout(m_gridBoss);

    m_tabs->addTab(pageInv,    tr("Inventory"));
    m_tabs->addTab(pagePeggle, tr("Peggle Levels"));
    m_tabs->addTab(pageBoss,   tr("Boss Level"));

    m_central->setLayout(m_vbox);
    setCentralWidget(m_central);
    setWindowTitle(tr("Inventory / Peggle / Boss Generator"));
    resize(600, 300);

    // 3) Connect signal
    connect(m_button, &QPushButton::clicked,
            this, &MainWindow::onGenerateClicked);

    // 4) Do NOT reseed the global generator here.
}

MainWindow::~MainWindow()
{
}

void MainWindow::setupNameMaps()
{
    // inventory names (1–10)
    invNames = {
        {}, "Bjorn", "Jimmy Lighting", "Kat Tut", "Spork",
        "Caddle", "Reinfield", "Tula", "Magic Hat",
        "Nice Green Dragon", "Master Xu"
    };

    // pegle level names (1–50)
    levelNames = { {} };
    levelNames << "Peggleland" << "Slip and Slide" << "Bjorn's Gazebo"
               << "Das Bucket" << "Snow Day" << "Birdy's Crib"
               << "Buffalo Wings" << "Skate Park" << "Spiral of Doom"
               << "Mr. Peepers" << "Scarab Crunch" << "Infinite Cheese"
               << "Ra Deal" << "Croco-Gator Pit" << "The Fever Level"
               << "The Amoeban" << "The Last Flower" << "We Come In Peace"
               << "Maid In Space" << "Getting The Spare" << "Pearl Clam"
               << "Insane Aquarium" << "Tasty Waves" << "Our Favorite Eel"
               << "Love Story" << "Waves" << "Spiderweb" << "Blockers"
               << "Baseball" << "Vermin" << "Holland Oats" << "I Heart Flowers"
               << "Workin From Home" << "Tula's Ride" << "70 and Sunny"
               << "Win a Monkey" << "Dog Pinball" << "Spin Again"
               << "Roll 'em" << "Five of a Kind" << "The Love Moat"
               << "Doom with a View" << "Rhombi" << "9 Luft Ballons"
               << "Twister Sisters" << "Spin Cycle" << "The Dude Abides"
               << "When Pigs Fly" << "Yang, Yin" << "Zen Frog";

    // boss names need slots 51..55
    bossNames.clear();
    bossNames.resize(56);  // indices 0 through 55 are now valid
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
    // clear all three grids
    clearLayout(m_gridInv);
    clearLayout(m_gridPeggle);
    clearLayout(m_gridBoss);

    // 2) Inventory (3×1), values 1–10
    for (int i = 0; i < 3; ++i) {
        int n = QRandomGenerator::global()->bounded(1, 11);
        QString txt = QString::asprintf("%02d %s",
                                        n, qPrintable(invNames[n]));
        auto *lbl = new QLabel(txt);
        lbl->setAlignment(Qt::AlignCenter);
        m_gridInv->addWidget(lbl, 0, i);
    }

    // 3) Peggle Levels (8×2, 15 cells), first 1–50, second 1–10
    for (int i = 0; i < 15; ++i) {
        int d1 = QRandomGenerator::global()->bounded(1, 51);
        int d2 = QRandomGenerator::global()->bounded(1, 11);
        QString txt = QString::asprintf("%02d %s, %02d %s",
                                        d1, qPrintable(levelNames[d1]),
                                        d2, qPrintable(invNames[d2]));
        auto *lbl = new QLabel(txt);
        lbl->setAlignment(Qt::AlignCenter);
        int row = i / 8, col = i % 8;
        m_gridPeggle->addWidget(lbl, row, col);
    }

    // 4) Boss Level (1×1), first 51–55, second 1–10
    {
        int d1 = QRandomGenerator::global()->bounded(51, 56);
        int d2 = QRandomGenerator::global()->bounded(1, 11);
        QString txt = QString::asprintf("%02d %s, %02d %s",
                                        d1, qPrintable(bossNames[d1]),
                                        d2, qPrintable(invNames[d2]));
        auto *lbl = new QLabel(txt);
        lbl->setAlignment(Qt::AlignCenter);
        m_gridBoss->addWidget(lbl, 0, 0);
    }
}
