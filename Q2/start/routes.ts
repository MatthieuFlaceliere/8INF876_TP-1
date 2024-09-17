/*
|--------------------------------------------------------------------------
| Routes file
|--------------------------------------------------------------------------
|
| The routes file is used for defining the HTTP routes.
|
*/

import router from '@adonisjs/core/services/router'
const ImcController = () => import('#controllers/imc_controller')

router.get('/', [ImcController, 'index'])
router.post('/imc', [ImcController, 'calculate'])
