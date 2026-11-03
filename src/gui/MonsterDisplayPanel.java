package gui;

import javax.swing.*;
import game.Monster;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * MonsterDisplayPanel - Displays monster tiles in a responsive grid
 * Maxed-out Cyber-Neon Arcade & 3D Volumetric Visuals.
 */
public class MonsterDisplayPanel extends JPanel {
    
    private ArrayList<Monster> monsters;
    private ArrayList<MonsterTile> tiles;
    private int highlightedIndex = -1;
    
    // Hyper-Neon Palette
    private static final Color BG_VOID       = new Color(10, 5, 22);
    private static final Color ELECTRIC_CYAN = new Color(0, 255, 242);
    private static final Color HYPER_MAGENTA = new Color(255, 0, 150);
    private static final int TILE_SPACING    = 16;
    
    /**
     * Constructor
     */
    public MonsterDisplayPanel() {
        monsters = new ArrayList<>();
        tiles = new ArrayList<>();
        setBackground(BG_VOID);
        
        // High-Contrast Cyber Header Border
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HYPER_MAGENTA, 2),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(6, 8, 6, 8),
                "⚡ ❮ MONSTER MATRIX ❯ ⚡",
                0, 0, new Font("Impact", Font.PLAIN, 20), ELECTRIC_CYAN)
        ));
    }
    
    /**
     * Set the monsters to display
     * @param monsters The list of monsters
     */
    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
        this.highlightedIndex = -1;
        createTiles();
    }
    
    /**
     * Highlight a specific monster
     * @param index The index to highlight (-1 for none)
     */
    public void highlightMonster(int index) {
        this.highlightedIndex = index;
        repaint();
    }
    
    /**
     * Create tiles for all monsters
     */
    private void createTiles() {
        removeAll();
        tiles.clear();
        
        if (monsters == null || monsters.isEmpty()) {
            JLabel emptyLabel = new JLabel("⚡ NO MONSTERS DETECTED ⚡");
            emptyLabel.setFont(new Font("Impact", Font.PLAIN, 18));
            emptyLabel.setForeground(ELECTRIC_CYAN);
            add(emptyLabel);
            revalidate();
            repaint();
            return;
        }
        
        // Determine grid layout
        int monsterCount = monsters.size();
        int cols = (int) Math.ceil(Math.sqrt(monsterCount));
        int rows = (int) Math.ceil((double) monsterCount / cols);
        
        setLayout(new GridLayout(rows, cols, TILE_SPACING, TILE_SPACING));
        
        // Create a tile for each monster
        for (int i = 0; i < monsters.size(); i++) {
            MonsterTile tile = new MonsterTile(monsters.get(i), i);
            tiles.add(tile);
            add(tile);
        }
        
        revalidate();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Retro Sci-Fi Grid lines in background
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 255, 242, 12));
        for (int y = 0; y < getHeight(); y += 20) {
            g2.drawLine(0, y, getWidth(), y);
        }
        for (int x = 0; x < getWidth(); x += 20) {
            g2.drawLine(x, 0, x, getHeight());
        }
        g2.dispose();

        // Update highlighting
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setHighlighted(i == highlightedIndex);
        }
    }
    
    /**
     * MonsterTile - Ultra-Vibrant 3D Holographic Card
     */
    private class MonsterTile extends JPanel {
        private Monster monster;
        private int index;
        private boolean highlighted = false;
        
        // Hyper-Neon Colors
        private static final Color NEON_YELLOW = new Color(255, 247, 0);
        private static final Color NEON_LIME   = new Color(57, 255, 20);
        private static final Color NEON_ORANGE = new Color(255, 120, 0);
        private static final Color NEON_PURPLE = new Color(200, 0, 255);
        private static final Color NEON_RED    = new Color(255, 0, 80);
        
        public MonsterTile(Monster monster, int index) {
            this.monster = monster;
            this.index = index;
            setLayout(new BorderLayout(8, 8));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            createLayout();
        }
        
        private void createLayout() {
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            
            // Bold Arcade Header
            JLabel numberLabel = new JLabel("UNIT #" + index);
            numberLabel.setFont(new Font("Impact", Font.PLAIN, 18));
            numberLabel.setForeground(ELECTRIC_CYAN);
            numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Ultra-bright status text
            JLabel healthLabel = new JLabel("HP  " + monster.health());
            healthLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            healthLabel.setForeground(NEON_LIME);
            healthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel damageLabel = new JLabel("ATK " + monster.damage());
            damageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            damageLabel.setForeground(NEON_ORANGE);
            damageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel speedLabel = new JLabel("SPD " + monster.speed());
            speedLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            speedLabel.setForeground(ELECTRIC_CYAN);
            speedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Glowing special tag
            String special = monster.special();
            if (special != null && !special.isEmpty()) {
                Font specialFont = new Font("SansSerif", Font.BOLD | Font.ITALIC, 11);
                JLabel specialLabel = new JLabel(Emoji.fit("★ " + special, specialFont));
                specialLabel.setForeground(NEON_YELLOW);
                specialLabel.setFont(specialFont);
                specialLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                infoPanel.add(specialLabel);
                infoPanel.add(Box.createVerticalStrut(4));
            }
            
            infoPanel.add(numberLabel);
            infoPanel.add(Box.createVerticalStrut(6));
            infoPanel.add(healthLabel);
            infoPanel.add(damageLabel);
            infoPanel.add(speedLabel);
            
            add(infoPanel, BorderLayout.CENTER);
            add(new HealthBar(), BorderLayout.SOUTH);
        }
        
        public void setHighlighted(boolean highlighted) {
            if (this.highlighted != highlighted) {
                this.highlighted = highlighted;
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            boolean isDead = monster.health() <= 0;
            
            // 1. Multi-layered 3D Outer Glow & Drop Shadow
            if (highlighted) {
                // Intense Yellow Aura
                g2.setColor(new Color(255, 247, 0, 90));
                g2.fillRoundRect(0, 0, w, h, 22, 22);
            } else {
                // Soft Cyan Shadow Depth
                g2.setColor(new Color(0, 255, 242, 30));
                g2.fillRoundRect(4, 6, w - 8, h - 6, 20, 20);
            }
            
            // 2. High-Contrast Gradient Core
            Color topBg = isDead ? new Color(70, 0, 30)  : new Color(35, 20, 65);
            Color botBg = isDead ? new Color(25, 0, 10)  : new Color(15, 10, 35);
            GradientPaint cardBg = new GradientPaint(0, 0, topBg, 0, h, botBg);
            g2.setPaint(cardBg);
            g2.fillRoundRect(3, 3, w - 6, h - 8, 18, 18);
            
            // 3. Curved Glass Reflection Beam
            Path2D gloss = new Path2D.Double();
            gloss.moveTo(3, 3);
            gloss.lineTo(w - 3, 3);
            gloss.lineTo(w - 3, h * 0.45);
            gloss.curveTo(w * 0.5, h * 0.35, w * 0.2, h * 0.5, 3, h * 0.4);
            gloss.closePath();
            
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 55), 0, (float)(h * 0.45), new Color(255, 255, 255, 0)));
            g2.fill(gloss);
            
            // 4. Multi-Stroke Beveled 3D Border
            if (highlighted) {
                g2.setColor(NEON_YELLOW);
                g2.setStroke(new BasicStroke(4f));
                g2.drawRoundRect(2, 2, w - 5, h - 7, 18, 18);
            } else {
                // Neon Pink/Cyan Dual Rim
                g2.setColor(isDead ? NEON_RED : HYPER_MAGENTA);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(3, 3, w - 7, h - 9, 18, 18);
                
                g2.setColor(ELECTRIC_CYAN);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(5, 5, w - 11, h - 13, 14, 14);
            }
            
            g2.dispose();
            super.paintComponent(g);
        }
        
        /**
         * Ultra-Bright 3D Segmented Health Gauge
         */
        private class HealthBar extends JPanel {
            private static final int BAR_HEIGHT = 20;
            
            public HealthBar() {
                setPreferredSize(new Dimension(0, BAR_HEIGHT));
                setOpaque(false);
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Recessed Dark Metal Base
                g2.setColor(new Color(5, 5, 15));
                g2.fillRoundRect(0, 0, w, h, 10, 10);
                
                double healthPercent = Math.max(0, Math.min(100, monster.health())) / 100.0;
                int barWidth = (int) (w * healthPercent);
                
                if (barWidth > 0) {
                    // Pick High-Intensity Neon Colors
                    Color topColor, botColor;
                    if (healthPercent > 0.5) {
                        topColor = NEON_LIME;
                        botColor = new Color(0, 180, 50);
                    } else if (healthPercent > 0.25) {
                        topColor = NEON_YELLOW;
                        botColor = new Color(255, 130, 0);
                    } else {
                        topColor = NEON_RED;
                        botColor = new Color(150, 0, 40);
                    }
                    
                    // 3D Metallic Gradient Fill
                    g2.setPaint(new GradientPaint(0, 0, topColor, 0, h, botColor));
                    g2.fillRoundRect(0, 0, barWidth, h, 10, 10);
                    
                    // Hot-White Highlighting Line
                    g2.setColor(new Color(255, 255, 255, 140));
                    g2.fillRect(0, 2, barWidth, h / 3);
                }
                
                // Outer Cyber Frame Accent
                g2.setColor(ELECTRIC_CYAN);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
                
                g2.dispose();
            }
        }
    }
}