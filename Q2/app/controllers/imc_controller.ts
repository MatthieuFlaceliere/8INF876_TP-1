import { IMCServices } from '#services/imc_service'
import { imcValidator } from '#validators/imc'
import type { HttpContext } from '@adonisjs/core/http'

export default class ImcController {
  public async calculate({ view, request }: HttpContext) {
    let { weight, height } = await request.validateUsing(imcValidator)

    return view.render('pages/home', { imc: IMCServices.calculate(weight, height) })
  }
}
