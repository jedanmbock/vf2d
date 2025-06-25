#VF2D

Une application permettant la résolution de l'équation différentielle -laplacien(u) = f et de visualiser l'ordre de convergence.

- exécuter GUI pour visualiser le cas de test pour f = -4 (solution exacte, solution numérique et erreur absolue)
- exécuter ConvergenceAnalysis pour voir la courbe ln(E) = g(ln(h)) avec h le pas.

Vous pouvez créer vos cas de test en ajoutant une classe implémentant l'interface *Function* dans le package *com.ananum.vf2d.functions*

u est la solution exacte que l'on veut obtenir et f est le second membre de l'équation.
