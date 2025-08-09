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
    m_vbox->addWidget(m_tabs, 1);
    m_vbox->addWidget(m_button, 0, Qt::AlignTop);

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
    // inventory names (1–10) as you first posted
    invNames = {
        {}, 
        "Bjorn", "Jimmy Lighting", "Kat Tut", "Spork",
        "Claude", "Reinfield", "Tula", "Warren",
        "Lord Cinderbottom", "Master Hu"
    };

    // pegle level names (1–50)
    levelNames.clear();
    levelNames.resize(51);
    QStringList lvl = {
        "Peggleland", "Slip and Slide", "Bjorn's Gazebo",
        "Das Bucket", "Snow Day", "Birdy's Crib",
        "Buffalo Wings", "Skate Park", "Spiral of Doom",
        "Mr. Peepers", "Scarab Crunch", "Infinite Cheese",
        "Ra Deal", "Croco-Gator Pit", "The Fever Level",
        "The Amoeban", "The Last Flower", "We Come In Peace",
        "Maid In Space", "Getting The Spare", "Pearl Clam",
        "Insane Aquarium", "Tasty Waves", "Our Favorite Eel",
        "Love Story", "Waves", "Spiderweb", "Blockers",
        "Baseball", "Vermin", "Holland Oats", "I Heart Flowers",
        "Workin From Home", "Tula's Ride", "70 and Sunny",
        "Win a Monkey", "Dog Pinball", "Spin Again",
        "Roll 'em", "Five of a Kind", "The Love Moat",
        "Doom with a View", "Rhombi", "9 Luft Ballons",
        "Twister Sisters", "Spin Cycle", "The Dude Abides",
        "When Pigs Fly", "Yang, Yin", "Zen Frog"
    };
    for (int i = 0; i < lvl.size(); ++i)
        levelNames[i+1] = lvl.at(i);

    // boss names (51–55)
    bossNames.clear();
    bossNames.resize(56);
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
    // 1) Clear previous widgets
    clearLayout(m_gridInv);
    clearLayout(m_gridPeggle);
    clearLayout(m_gridBoss);

    // 2) Inventory (3×1) – only show the name
    for (int i = 0; i < 3; ++i) {
        int n = QRandomGenerator::global()->bounded(1, invNames.size());
        QString txt = invNames[n];                       // ← no "NN " prefix
        auto *lbl = new QLabel(txt);
        lbl->setAlignment(Qt::AlignCenter);
        m_gridInv->addWidget(lbl, 0, i);
    }

    // 3) Peggle Levels (8×2, 15 cells) – show "LevelName, InventoryName"
    for (int i = 0; i < 15; ++i) {
        int d1 = QRandomGenerator::global()->bounded(1, levelNames.size());
        int d2 = QRandomGenerator::global()->bounded(1, invNames.size());
        QString txt = QStringLiteral("%1, %2")
                          .arg(levelNames[d1])
                          .arg(invNames[d2]);            // ← drop the numbers
        auto *lbl = new QLabel(txt);
        lbl->setAlignment(Qt::AlignCenter);
        int row = i / 8, col = i % 8;
        m_gridPeggle->addWidget(lbl, row, col);
    }

    // 4) Boss Level (1×1) – show "BossName, InventoryName"
    {
        int d1 = QRandomGenerator::global()->bounded(51, bossNames.size());
        int d2 = QRandomGenerator::global()->bounded(1,  invNames.size());
        QString txt = QStringLiteral("%1, %2")
                          .arg(bossNames[d1])
                          .arg(invNames[d2]);            // ← numbers still used internally
        auto *lbl = new QLabel(txt);
        lbl->setAlignment(Qt::AlignCenter);
        m_gridBoss->addWidget(lbl, 0, 0);
    }
}
