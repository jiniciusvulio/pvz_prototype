import javax.swing.*;
import java.awt.*;
import java.util.Random;

    public class GameGUI extends JFrame{

        private int level = 1;
        private int coins = 0;
        private final int COST_UPGRADE_HP = 30;
        private final int COST_UPGRADE_DAMAGE = 40;

    
        private Plant plant;
        private Zombie zombie;
        
        private JLabel plantLabel, zombieLabel, statusLabel;
        private JProgressBar plantHealthBar, zombieHealthBar;
        private JButton startBattleButton, nextTurnButton, restartButton, autoBattleButton;
        private JComboBox<String> plantSelect, zombieSelect;

        private PrototypeRegistry registry;
        private Timer autoBattleTimer;
        private Random random = new Random();
        
        
        public GameGUI(){
            setTitle("Design Patterns - Prototype: PvZ");
            setSize(450, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10,10));
                        
            // registros
            registry = new PrototypeRegistry();
            
            // plantas
            registry.addPrototype("Pea Shooter", new Plant("Pea Shooter", 100, 20));
            registry.addPrototype("WallNut", new Plant("WallNut", 200, 0));
            
            // zumbis
            registry.addPrototype("Basic Zombie", new Zombie("Basic Zombie", 80, 10));
            registry.addPrototype("Conehead Zombie", new Zombie("Conehead Zombie", 150, 10));            
            

            // selecao
            JPanel selectPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            
            selectPanel.add(new JLabel("Selecione a Planta:"));
            plantSelect = new JComboBox<>(new String[]{"Pea Shooter", "WallNut"});
            selectPanel.add(plantSelect);
            
            selectPanel.add(new JLabel("Selecione o Zumbi:"));
            zombieSelect = new JComboBox<>(new String[]{"Basic Zombie", "Conehead Zombie"});
            selectPanel.add(zombieSelect);
            
            startBattleButton = new JButton("Iniciar Batalha");
            selectPanel.add(startBattleButton);
            
            nextTurnButton = new JButton("Proximo turno");
            nextTurnButton.setEnabled(false);
            selectPanel.add(nextTurnButton);
            
            add(selectPanel, BorderLayout.NORTH);
            
            
            // status e barras de hp CENTRO
            JPanel statusPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            
            plantHealthBar = new JProgressBar();
            zombieHealthBar = new JProgressBar();
            
            statusPanel.add(new JLabel("HP da Planta:"));
            statusPanel.add(plantHealthBar);
            statusPanel.add(new JLabel("HP do Zumbi:"));
            statusPanel.add(zombieHealthBar);
            add(statusPanel, BorderLayout.CENTER);

            // status e botao de reiniciar
            JPanel bottomPanel = new JPanel(new BorderLayout());          
            statusLabel = new JLabel("Selecione os personagens para iniciar.", SwingConstants.CENTER);

            restartButton = new JButton("Reiniciar");
            restartButton.setEnabled(false);

            autoBattleButton = new JButton("Modo automático");
            autoBattleButton.setEnabled(false);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(autoBattleButton);
            buttonPanel.add(restartButton);

            bottomPanel.add(statusLabel, BorderLayout.CENTER);
            bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
            add(bottomPanel, BorderLayout.SOUTH);


            // aqyu é o que os boitoes fazem
            startBattleButton.addActionListener(e -> startBattle());
            nextTurnButton.addActionListener(e -> playTurn());
            autoBattleButton.addActionListener(e -> toggleAutoBattle());
            restartButton.addActionListener(e -> restartGame());
        }

        // nova batalha
        private void startBattle(){
            String selectedPlant = (String) plantSelect.getSelectedItem();
            String selectedZombie = (String) zombieSelect.getSelectedItem();

            plant = (Plant) registry.getClone(selectedPlant);
            zombie = (Zombie) registry.getClone(selectedZombie);

            plantHealthBar.setMaximum(plant.health);
            plantHealthBar.setValue(plant.health);
            zombieHealthBar.setMaximum(zombie.health);
            zombieHealthBar.setValue(zombie.health);

            plantSelect.setEnabled(false);
            zombieSelect.setEnabled(false);
            startBattleButton.setEnabled(false);
            nextTurnButton.setEnabled(true);
            autoBattleButton.setEnabled(true);
            restartButton.setEnabled(false);

            statusLabel.setText("Batalha iniciando! " + plant.name + " vs " + zombie.name);
        }

        private void playTurn(){
            if(plant.isAlive() && zombie.isAlive()){
                int plantDamage = plant.isDefensive() ? 0 : getVariableDamage(plant.damage);
                int zombieDamage = getVariableDamage(zombie.damage);

                zombie.health -= plantDamage;
                plant.health -= zombieDamage;

                if(zombie.health < 0) zombie.health = 0;
                if(plant.health < 0) plant.health = 0;

                plantHealthBar.setValue(plant.health);
                zombieHealthBar.setValue(zombie.health);

                animateBarDamage(plantHealthBar);
                animateBarDamage(zombieHealthBar);

                if(!plant.isAlive() || !zombie.isAlive()){
                    boolean plantWon = plant.isAlive();
                    String winner = plant.isAlive() ? plant.name + " venceu!" : zombie.name + " venceu!";
                    statusLabel.setText(winner);

                    nextTurnButton.setEnabled(false);
                    autoBattleButton.setEnabled(false);
                    restartButton.setEnabled(true);

                    if(plantWon){
                        int reward = 10 * level;
                        coins += reward;

                        JOptionPane.showMessageDialog(
                            this, 
                            "🌻" + plant.name + " venceu o nível " + level + "!\n" + 
                            "Recompensa: " + reward + " moedas!\n" +
                            "Moedas: " + coins,
                            "Vitória!", JOptionPane.INFORMATION_MESSAGE
                        );
                        startNextLevel();

                        openShop();

                        showLevelTransition(level + 1);
                    }

                }else{
                    statusLabel.setText("A batalha continua...");
                }
            }
        }

        private void openShop(){
            String[] options = {"Upgrade HP (" + COST_UPGRADE_HP + " moedas)", "Upgrade Dano (" + COST_UPGRADE_DAMAGE + " moedas)", "Continuar"};
            int choice = JOptionPane.showOptionDialog(
                this,
                "Loja de Melhorias - Moedas " + coins,
                "Loja de Melhorias",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );

            if(choice == 0 && coins >= COST_UPGRADE_HP){
                plant.health += 30;
                coins -= COST_UPGRADE_HP;
                JOptionPane.showMessageDialog(this, "HP da planta aumentado! Moedas restantes: " + coins);
            }else if(choice == 1 && coins >= COST_UPGRADE_DAMAGE){
                plant.damage += 5;
                coins -= COST_UPGRADE_DAMAGE;
                JOptionPane.showMessageDialog(this, "Dano da planta aumentado! Moedas restantes: " + coins);
            }else if(choice == 0 || choice == 1){
                JOptionPane.showMessageDialog(this, "Moedas insuficientes!");
            }
        }

        private void showLevelTransition(int nextLevel){
            level = nextLevel;
            JOptionPane.showMessageDialog(
                this,
                "Avançando para o Nível " + level + "!\n Os zumbis estão ficando mais fortes...",
                "Nível " + level,
                JOptionPane.INFORMATION_MESSAGE
            );
            startNextLevel();
        }

        private void startNextLevel(){
            String selectedZombie = (String) zombieSelect.getSelectedItem();
            zombie = (Zombie) registry.getClone(selectedZombie);
            zombie.health += level * 20; // zumbis mais fortes a cada nivel
            zombie.damage += level * 2;

            plant.health = Math.min(plant.health + 30, plantHealthBar.getMaximum()); // planta recupera um pouco de vida

            plantHealthBar.setMaximum(plant.health);
            plantHealthBar.setValue(plant.health);
            zombieHealthBar.setMaximum(zombie.health);
            zombieHealthBar.setValue(zombie.health);

            statusLabel.setText("Nível " + level + ": nova batalha começando!");
            nextTurnButton.setEnabled(true);
            autoBattleButton.setEnabled(true);
            restartButton.setEnabled(false);
        }


        // pra nao ficar previsivel o dano vai ser aleatorio
        private int getVariableDamage(int baseDamage){
            double factor = 0.7 + (random.nextDouble() * 0.6); // entre 0.7 e 1.3
            return (int) Math.round(baseDamage * factor);
        }

        // anim simples de dano piscanbdo a barra
        private void animateBarDamage(JProgressBar bar){
            Color originalColor = bar.getForeground();
            bar.setForeground(Color.RED);
            Timer timer = new Timer(200, e -> bar.setForeground(originalColor));
            timer.setRepeats(false);
            timer.start();
        }

        // liga ou desliga o modo automatico
        private void toggleAutoBattle(){
            if(autoBattleTimer != null && autoBattleTimer.isRunning()){
                autoBattleTimer.stop();
                autoBattleButton.setText("Modo automático");
                nextTurnButton.setEnabled(true);
            }else{
                autoBattleTimer = new Timer(1000, e -> playTurn());
                autoBattleTimer.start();
                autoBattleButton.setText("Parar automático");
                nextTurnButton.setEnabled(false);
            }
        }

    private void restartGame(){
        if(autoBattleTimer != null){
            autoBattleTimer.stop();
        }

        level = 1;
        coins = 0;

        plantSelect.setEnabled(true);
        zombieSelect.setEnabled(true);
        startBattleButton.setEnabled(true);
        nextTurnButton.setEnabled(false);
        autoBattleButton.setEnabled(false);
        restartButton.setEnabled(false);
        autoBattleButton.setText("Modo automático");

        plantHealthBar.setValue(0);
        zombieHealthBar.setValue(0);

        statusLabel.setText("Selecione os personagens para iniciar.");

    }



    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(()->{
            new GameGUI().setVisible(true);
        });
    }
    
    
    
}
