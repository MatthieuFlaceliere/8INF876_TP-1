import { IMCServices } from '#services/imc_service'
import { imcValidator } from '#validators/imc'
import type { HttpContext } from '@adonisjs/core/http'

export default class ImcController {
  public async index({ view }: HttpContext) {
    // Récupérer tous les enregistrements d'IMC
    const imcRecords = await IMCServices.getAllImcRecords()

    // Afficher le résultat
    return view.render('pages/home', { imcRecords })
  }

  public async calculate({ view, request }: HttpContext) {
    let { weight, height } = await request.validateUsing(imcValidator)

    // Calculer et enregistrer l'IMC
    const imcRecord = await IMCServices.calculateAndSave(weight, height)

    // Récupérer tous les enregistrements d'IMC
    const imcRecords = await IMCServices.getAllImcRecords()

    // Afficher le résultat
    return view.render('pages/home', { imc: imcRecord.imc, imcRecords })
  }
}
